package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.enums.IntrabankTransactionStatus;
import com.imannuel.movin.intrabanktransferservice.repository.IntraBankTransactionStatusRepository;
import com.imannuel.movin.intrabanktransferservice.service.IntraBankTransactionStatusService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntraBankTransactionStatusServiceImpl implements IntraBankTransactionStatusService {
    private final IntraBankTransactionStatusRepository intraBankTransactionStatusRepository;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    void init() {
        log.info("Initializing roles");
        for (IntrabankTransactionStatus.EIntraBankTransactionStatus value : IntrabankTransactionStatus.EIntraBankTransactionStatus.values()) {
            if (!checkTransactionStatusExists(value.name())) {
                log.debug("Transaction status {} not found, creating it", value.name());
                IntrabankTransactionStatus intrabankTransactionStatus = IntrabankTransactionStatus.builder()
                        .status(value)
                        .build();
                intraBankTransactionStatusRepository.save(intrabankTransactionStatus);
                log.info("Transaction status {} created successfully", value.name());
            } else {
                log.debug("Transaction status {} already exists, skipping creation", value.name());
            }
        }
        log.info("Transaction status initialization completed");
    }

    @Override
    public IntrabankTransactionStatus findTransactionStatus(String transactionStatus) {
        log.debug("Searching for transaction status: {}", transactionStatus);
        try {
            IntrabankTransactionStatus foundIntrabankTransactionStatus = intraBankTransactionStatusRepository.findByStatus(
                    IntrabankTransactionStatus.EIntraBankTransactionStatus.valueOf(transactionStatus)
            ).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction status not found")
            );
            log.debug("Found transaction status: {}", transactionStatus);
            return foundIntrabankTransactionStatus;
        } catch (Exception e) {
            log.warn("Transaction status not found: {}", transactionStatus);
            throw e;
        }
    }

    @Override
    public boolean checkTransactionStatusExists(String status) {
        log.debug("Checking availability for transaction status: {}", status);
        try {
            boolean exists = intraBankTransactionStatusRepository.existsByStatus(
                    IntrabankTransactionStatus.EIntraBankTransactionStatus.valueOf(status)
            );
            log.debug("Transaction status {} availability: {}", status, exists);
            return exists;
        } catch (Exception e) {
            log.error("Invalid transaction status while checking availability: {}", status, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction status name", e);
        }
    }
}
