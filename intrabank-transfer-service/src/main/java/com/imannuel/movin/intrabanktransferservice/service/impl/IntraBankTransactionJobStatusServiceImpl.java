package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.enums.IntraBankTransactionJobStatus;
import com.imannuel.movin.intrabanktransferservice.repository.IntraBankTransactionJobStatusRepository;
import com.imannuel.movin.intrabanktransferservice.service.IntraBankTransactionJobStatusService;
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
public class IntraBankTransactionJobStatusServiceImpl implements IntraBankTransactionJobStatusService {
    private final IntraBankTransactionJobStatusRepository repository;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    void init() {
        log.info("Initializing intra bank transaction job status");
        for (IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus value : IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus.values()) {
            if (!checkIntraBankTransactionJobStatusExists(value.name())) {
                log.debug("Intra bank transaction job status {} not found, creating it", value.name());
                IntraBankTransactionJobStatus intraBankTransactionJobStatus = IntraBankTransactionJobStatus.builder()
                        .status(value)
                        .build();
                repository.save(intraBankTransactionJobStatus);
                log.info("Intra bank transaction job status {} created successfully", value.name());
            } else {
                log.debug("Intra bank transaction job status {} already exists, skipping creation", value.name());
            }
        }
        log.info("Intra bank transaction job status initialization completed");
    }

    @Override
    public IntraBankTransactionJobStatus findIntraBankTransactionJobStatus(String intraBankTransactionJobStatus) {
        log.debug("Searching for intra bank transaction job status: {}", intraBankTransactionJobStatus);
        try {
            IntraBankTransactionJobStatus foundIntraBankTransactionJobStatus = repository.findByStatus(
                    IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus.valueOf(intraBankTransactionJobStatus)
            ).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Intra bank transaction job status not found")
            );
            log.debug("Found intra bank transaction job status: {}", intraBankTransactionJobStatus);
            return foundIntraBankTransactionJobStatus;
        } catch (Exception e) {
            log.warn("Intra bank transaction job status not found: {}", intraBankTransactionJobStatus);
            throw e;
        }
    }

    @Override
    public boolean checkIntraBankTransactionJobStatusExists(String status) {
        log.debug("Checking availability for intra bank transaction job status: {}", status);
        try {
            boolean exists = repository.existsByStatus(
                    IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus.valueOf(status)
            );
            log.debug("Intra bank transaction job status {} availability: {}", status, exists);
            return exists;
        } catch (Exception e) {
            log.error("Invalid intra bank transaction job status while checking availability: {}", status, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid intra bank transaction job status", e);
        }
    }
}
