package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.dto.mapper.IntraBankTransactionMapper;
import com.imannuel.movin.intrabanktransferservice.dto.request.CreateIntraBankTransactionRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.IntraBankTransactionResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.IntraBankTransferResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.BankFeignResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.UserFeignResponse;
import com.imannuel.movin.intrabanktransferservice.entity.BankAccount;
import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransaction;
import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransactionJob;
import com.imannuel.movin.intrabanktransferservice.enums.IntrabankTransactionStatus;
import com.imannuel.movin.intrabanktransferservice.repository.IntraBankTransactionRepository;
import com.imannuel.movin.intrabanktransferservice.service.*;
import com.imannuel.movin.intrabanktransferservice.utility.CurrencyUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
@EnableAsync
public class IntraBankTransactionServiceImpl implements IntraBankTransactionService {
    private final IntraBankTransactionRepository repository;
    private final IntraBankTransactionStatusService intraBankTransactionStatusService;
    private final IntraBankTransactionJobService intraBankTransactionJobService;
    private final IntraBankTransactionJobDetailService intraBankTransactionJobDetailService;
    private final BankFeignClientService bankFeignClientService;
    private final BankServiceFactory bankServiceFactory;
    private final BankAccountService bankAccountService;
    private final UserFeignClientService userFeignClientService;
    private final RedisService redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IntraBankTransactionResponse createIntraBankTransaction(String senderId, CreateIntraBankTransactionRequest request) {
        log.info("Creating intra-bank transaction for senderId: {}", senderId);

        UserFeignResponse user = userFeignClientService.findUserByUserId(senderId);
        BankFeignResponse senderBank = bankFeignClientService.findBankByCode(request.getSenderBankCode());
        BankFeignResponse recipientBank = bankFeignClientService.findBankByCode(request.getRecipientBankCode());
        BankAccount recipientBankAccountInquiry = bankAccountService.getBankAccountInquiry(recipientBank, request.getRecipientAccountNumber());

        IntrabankTransactionStatus transactionStatus = intraBankTransactionStatusService
                .findTransactionStatus(IntrabankTransactionStatus.EIntraBankTransactionStatus.WAITING_PAYMENT.name());
        Long uniqueCode = (long) Math.max(recipientBank.getFee(), CurrencyUtility.generateUniqueCode(recipientBank.getFee()));

        IntraBankTransaction intraBankTransaction = IntraBankTransaction.builder()
                .senderId(user.getId())
                .senderBankId(senderBank.getId())
                .amount(request.getAmount())
                .uniqueCode(uniqueCode)
                .recipientAccountNumber(request.getRecipientAccountNumber())
                .recipientBankId(recipientBank.getId())
                .remark(request.getRemark())
                .timeRequest(LocalDateTime.now())
                .status(transactionStatus)
                .build();
        repository.saveAndFlush(intraBankTransaction);

        redisService.save(
                String.format("expired-intra-bank-transaction:%s", intraBankTransaction.getId()),
                String.format("%s:%s:%s", intraBankTransaction.getSenderId(), intraBankTransaction.getAmount(), intraBankTransaction.getUniqueCode()),
                Duration.ofMinutes(15)
        );

        log.info("Created intra-bank transaction with id: {}", intraBankTransaction.getId());
        return IntraBankTransactionMapper.toIntraBankTransactionResponse(
                intraBankTransaction, senderBank, recipientBank, recipientBankAccountInquiry
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IntraBankTransactionResponse receiveIntraBankTransactionPayment(String transactionId) {
        log.info("Receiving payment for intra-bank transaction with id: {}", transactionId);

        IntraBankTransaction intraBankTransaction = findIntraBankTransaction(transactionId);

        if (!intraBankTransaction.getStatus().getStatus().equals(IntrabankTransactionStatus.EIntraBankTransactionStatus.WAITING_PAYMENT)) {
            log.warn("Transaction {} cannot be updated, current status: {}", transactionId, intraBankTransaction.getStatus().getStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Intra bank transaction can't be updated");
        }

        IntrabankTransactionStatus pendingTransactionStatus = intraBankTransactionStatusService.findTransactionStatus(IntrabankTransactionStatus.EIntraBankTransactionStatus.PENDING.name());
        intraBankTransaction.setStatus(pendingTransactionStatus);
        repository.saveAndFlush(intraBankTransaction);

        log.info("Updated transaction {} status to PENDING", transactionId);

        BankFeignResponse senderBank = bankFeignClientService.findBankById(intraBankTransaction.getSenderBankId());
        BankFeignResponse recipientBank = bankFeignClientService.findBankById(intraBankTransaction.getRecipientBankId());
        BankAccount recipientBankAccountInquiry = bankAccountService.getBankAccountInquiry(recipientBank, intraBankTransaction.getRecipientAccountNumber());

        return IntraBankTransactionMapper.toIntraBankTransactionResponse(
                intraBankTransaction, senderBank, recipientBank, recipientBankAccountInquiry
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<IntraBankTransaction> getAllPendingIntraBankTransaction() {
        log.info("Fetching all pending intra-bank transactions...");
        IntrabankTransactionStatus pendingTransactionStatus = intraBankTransactionStatusService.findTransactionStatus(IntrabankTransactionStatus.EIntraBankTransactionStatus.PENDING.name());
        return repository.findAllByStatus(pendingTransactionStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processIntraBankTransaction(IntraBankTransactionJob intraBankTransactionJob, IntraBankTransaction intraBankTransaction) {
        log.info("Processing intra-bank transaction with id: {}", intraBankTransaction.getId());

        BankFeignResponse recipientBank = bankFeignClientService.findBankById(intraBankTransaction.getRecipientBankId());
        BankService bankService = bankServiceFactory.getBankService(recipientBank.getCode());
        IntraBankTransferResponse intraBankTransferResponse = bankService.processIntrabankTransfer(intraBankTransaction.getRecipientAccountNumber(), intraBankTransaction.getAmount());

        if (intraBankTransferResponse.isSuccessfully()) {
            log.info("Transaction {} processed successfully", intraBankTransaction.getId());
            IntrabankTransactionStatus successTransactionStatus = intraBankTransactionStatusService.findTransactionStatus(IntrabankTransactionStatus.EIntraBankTransactionStatus.SUCCESS.name());
            intraBankTransaction.setStatus(successTransactionStatus);
            intraBankTransactionJob.setSuccessfulCount(intraBankTransactionJob.getSuccessfulCount() + 1);

            intraBankTransactionJobDetailService.createSuccessIntraBankTransactionJobDetail(intraBankTransactionJob, intraBankTransaction);
        } else {
            log.error("Transaction {} failed: {}", intraBankTransaction.getId(), intraBankTransferResponse.getMessage());
            IntrabankTransactionStatus failedTransactionStatus = intraBankTransactionStatusService.findTransactionStatus(IntrabankTransactionStatus.EIntraBankTransactionStatus.FAILED.name());
            intraBankTransaction.setStatus(failedTransactionStatus);
            intraBankTransactionJob.setFailedCount(intraBankTransactionJob.getFailedCount() + 1);

            intraBankTransactionJobDetailService.createFailedIntraBankTransactionJobDetail(intraBankTransactionJob, intraBankTransferResponse.getMessage(), intraBankTransaction);
        }

        intraBankTransaction.setTimeServed(LocalDateTime.now());
        repository.saveAndFlush(intraBankTransaction);
    }

    @Override
    @Scheduled(fixedRate = 60000)
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void processBatchIntraBankTransaction() {
        log.info("Starting batch processing for intra-bank transactions...");
        IntraBankTransactionJob intraBankTransactionJob = intraBankTransactionJobService.createIntraBankTransactionJob();
        List<IntraBankTransaction> allPendingIntraBankTransaction = getAllPendingIntraBankTransaction();

        allPendingIntraBankTransaction.forEach(intraBankTransaction ->
                processIntraBankTransaction(intraBankTransactionJob, intraBankTransaction));

        intraBankTransactionJob.setTotalTransactions(allPendingIntraBankTransaction.size());
        intraBankTransactionJobService.finishIntraBankTransactionJob(intraBankTransactionJob);
        log.info("Batch processing completed. Total transactions processed: {}", allPendingIntraBankTransaction.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processExpiredIntraBankTransaction(String transactionId) {
        log.warn("Processing expired intra-bank transaction with id: {}", transactionId);

        IntraBankTransaction intraBankTransaction = findIntraBankTransaction(transactionId);

        if (!intraBankTransaction.getStatus().getStatus().equals(IntrabankTransactionStatus.EIntraBankTransactionStatus.WAITING_PAYMENT)) {
            log.warn("Intra-bank transaction with id: {} already paid, skipping process", transactionId);
            return;
        }

        IntrabankTransactionStatus expiredTransactionStatus = intraBankTransactionStatusService.findTransactionStatus(IntrabankTransactionStatus.EIntraBankTransactionStatus.EXPIRED.name());
        intraBankTransaction.setStatus(expiredTransactionStatus);

        repository.save(intraBankTransaction);

        log.warn("Successfully updated intra-bank transaction with id: {} status to expired", transactionId);
    }

    @Override
    @Transactional(readOnly = true)
    public IntraBankTransaction findIntraBankTransaction(String id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Intra bank transaction with id {} not found", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Intra bank transaction not found");
        });
    }

    @Override
    @Transactional(readOnly = true)
    public IntraBankTransactionResponse getIntraBankTransaction(String id) {
        log.info("Fetching intra-bank transaction with id: {}", id);
        IntraBankTransaction intraBankTransaction = findIntraBankTransaction(id);
        BankFeignResponse senderBank = bankFeignClientService.findBankById(intraBankTransaction.getSenderBankId());
        BankFeignResponse recipientBank = bankFeignClientService.findBankById(intraBankTransaction.getRecipientBankId());
        BankAccount recipientBankAccountInquiry = bankAccountService.getBankAccountInquiry(recipientBank, intraBankTransaction.getRecipientAccountNumber());

        return IntraBankTransactionMapper.toIntraBankTransactionResponse(
                intraBankTransaction, senderBank, recipientBank, recipientBankAccountInquiry
        );
    }
}