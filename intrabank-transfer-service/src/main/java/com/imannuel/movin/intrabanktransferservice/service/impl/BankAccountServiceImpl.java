package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.commonservice.utility.ValidationUtility;
import com.imannuel.movin.intrabanktransferservice.dto.mapper.BankAccountMapper;
import com.imannuel.movin.intrabanktransferservice.dto.request.BankAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.BankAccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.BankFeignResponse;
import com.imannuel.movin.intrabanktransferservice.entity.BankAccount;
import com.imannuel.movin.intrabanktransferservice.enums.BankAccountStatus;
import com.imannuel.movin.intrabanktransferservice.repository.BankAccountRepository;
import com.imannuel.movin.intrabanktransferservice.service.BankAccountService;
import com.imannuel.movin.intrabanktransferservice.service.BankAccountStatusService;
import com.imannuel.movin.intrabanktransferservice.service.BankFeignClientService;
import com.imannuel.movin.intrabanktransferservice.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountStatusService bankAccountStatusService;
    private final BankFeignClientService bankFeignClientService;
    private final BankServiceFactory bankServiceFactory;
    private final ValidationUtility validationUtility;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BankAccount newBankAccountInquiry(BankFeignResponse bank, String accountNumber) {
        log.info("Initiating new bank account inquiry for bankCode: {} and accountNumber: {}", bank.getCode(), accountNumber);

        BankService bankService = bankServiceFactory.getBankService(bank.getCode());
        AccountInquiryResponse accountInquiry = bankService.getAccountInquiry(accountNumber);

        log.info("Received account inquiry response: accountHolder: {}, accountNumber: {}",
                accountInquiry.getAccountHolderName(), accountInquiry.getAccountNumber());

        BankAccountStatus bankAccountStatus = bankAccountStatusService.findBankAccountStatus(BankAccountStatus.EBankAccountStatus.SUCCESS.name());

        BankAccount bankAccount = BankAccount.builder()
                .bankId(bank.getId())
                .accountHolder(accountInquiry.getAccountHolderName())
                .accountNumber(accountInquiry.getAccountNumber())
                .bankAccountStatus(bankAccountStatus)
                .build();

        bankAccountRepository.saveAndFlush(bankAccount);
        log.info("New bank account inquiry saved: bankId: {}, accountNumber: {}", bank.getId(), accountInquiry.getAccountNumber());

        return bankAccount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BankAccountInquiryResponse getBankAccountInquiry(BankAccountInquiryRequest bankAccountInquiryRequest) {
        log.info("Processing bank account inquiry request: bankCode: {}, accountNumber: {}",
                bankAccountInquiryRequest.getBankCode(), bankAccountInquiryRequest.getAccountNumber());

        validationUtility.validate(bankAccountInquiryRequest);
        log.debug("Validation passed for bank account inquiry request");

        BankFeignResponse bank = bankFeignClientService.findBankByCode(bankAccountInquiryRequest.getBankCode());
        log.info("Fetched bank details: bankId: {}, bankCode: {}", bank.getId(), bank.getCode());

        Optional<BankAccount> bankAccount = bankAccountRepository.findByBankIdAndAccountNumber(
                bank.getId(), bankAccountInquiryRequest.getAccountNumber());

        if (bankAccount.isEmpty()) {
            log.warn("Bank account not found, performing new inquiry for accountNumber: {}", bankAccountInquiryRequest.getAccountNumber());
            BankAccount savedBankAccount = newBankAccountInquiry(bank, bankAccountInquiryRequest.getAccountNumber());

            return BankAccountMapper.toBankAccountInquiryResponse(bank, savedBankAccount);
        }

        log.info("Bank account found: bankId: {}, accountNumber: {}", bank.getId(), bankAccountInquiryRequest.getAccountNumber());
        return BankAccountMapper.toBankAccountInquiryResponse(bank, bankAccount.get());
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccount getBankAccountInquiry(BankFeignResponse bank, String accountNumber) {
        log.info("Retrieving bank account inquiry: bankId: {}, accountNumber: {}", bank.getId(), accountNumber);

        Optional<BankAccount> bankAccount = bankAccountRepository.findByBankIdAndAccountNumber(bank.getId(), accountNumber);

        if (bankAccount.isEmpty()) {
            log.warn("Bank account not found, performing new inquiry for accountNumber: {}", accountNumber);
            return newBankAccountInquiry(bank, accountNumber);
        }

        log.info("Bank account found: bankId: {}, accountNumber: {}", bank.getId(), accountNumber);
        return bankAccount.get();
    }
}