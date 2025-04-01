package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.enums.BankAccountStatus;
import com.imannuel.movin.intrabanktransferservice.repository.BankAccountStatusRepository;
import com.imannuel.movin.intrabanktransferservice.service.BankAccountStatusService;
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
public class BankAccountStatusServiceImpl implements BankAccountStatusService {
    private final BankAccountStatusRepository bankAccountStatusRepository;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    void init() {
        log.info("Initializing Bank Account Status");
        for (BankAccountStatus.EBankAccountStatus value : BankAccountStatus.EBankAccountStatus.values()) {
            if (!checkBankAccountStatusExists(value.name())) {
                log.debug("Bank Account Status {} not found, creating it", value.name());
                BankAccountStatus bankAccountStatus = BankAccountStatus.builder()
                        .status(value)
                        .build();
                bankAccountStatusRepository.save(bankAccountStatus);
                log.info("Bank Account Status {} created successfully", value.name());
            } else {
                log.debug("Bank Account Status {} already exists, skipping creation", value.name());
            }
        }
        log.info("Bank Account Status initialization completed");
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccountStatus findBankAccountStatus(String status) {
        log.debug("Searching for Bank Account Status: {}", status);
        try {
            BankAccountStatus foundBankAccountStatus = bankAccountStatusRepository
                    .findByStatus
                            (BankAccountStatus.EBankAccountStatus.valueOf(status)
                            )
                    .orElseThrow(() -> new ResponseStatusException
                            (HttpStatus.NOT_FOUND, "Bank Account Status not found")
                    );
            log.debug("Found Bank Account Status: {}", status);
            return foundBankAccountStatus;
        } catch (Exception e) {
            log.warn("Bank Account Status not found: {}", status);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkBankAccountStatusExists(String status) {
        log.debug("Checking availability for bank account status: {}", status);
        try {
            boolean exists = bankAccountStatusRepository.existsByStatus(
                    BankAccountStatus.EBankAccountStatus.valueOf(status)
            );
            log.debug("Bank Account Status {} availability: {}", status, exists);
            return exists;
        } catch (Exception e) {
            log.error("Invalid Bank Account Status name while checking availability: {}", status, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Bank Account Status name", e);
        }
    }
}
