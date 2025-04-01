package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransaction;
import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransactionJob;
import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransactionJobDetail;
import com.imannuel.movin.intrabanktransferservice.enums.IntraBankTransactionJobStatus;
import com.imannuel.movin.intrabanktransferservice.repository.IntraBankTransactionJobDetailRepository;
import com.imannuel.movin.intrabanktransferservice.service.IntraBankTransactionJobDetailService;
import com.imannuel.movin.intrabanktransferservice.service.IntraBankTransactionJobStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntraBankTransactionJobDetailServiceImpl implements IntraBankTransactionJobDetailService {
    private final IntraBankTransactionJobDetailRepository repository;
    private final IntraBankTransactionJobStatusService intraBankTransactionJobStatusService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSuccessIntraBankTransactionJobDetail(IntraBankTransactionJob intraBankTransactionJob, IntraBankTransaction intraBankTransaction) {
        log.info("Creating success intra-bank transaction job detail for jobId: {}, transactionId: {}",
                intraBankTransactionJob.getId(), intraBankTransaction.getId());

        IntraBankTransactionJobStatus successIntraBankTransactionJobStatus = intraBankTransactionJobStatusService
                .findIntraBankTransactionJobStatus(
                        IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus.COMPLETED.name()
                );

        IntraBankTransactionJobDetail intraBankTransactionJobDetail = IntraBankTransactionJobDetail.builder()
                .job(intraBankTransactionJob)
                .transaction(intraBankTransaction)
                .processedAt(LocalDateTime.now())
                .status(successIntraBankTransactionJobStatus)
                .build();

        repository.save(intraBankTransactionJobDetail);
        log.info("Successfully created job detail with id: {}", intraBankTransactionJobDetail.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFailedIntraBankTransactionJobDetail(IntraBankTransactionJob intraBankTransactionJob, String errorMessage, IntraBankTransaction intraBankTransaction) {
        log.info("Creating failed intra-bank transaction job detail for jobId: {}, transactionId: {}, error: {}",
                intraBankTransactionJob.getId(), intraBankTransaction.getId(), errorMessage);

        IntraBankTransactionJobStatus failedIntraBankTransactionJobStatus = intraBankTransactionJobStatusService
                .findIntraBankTransactionJobStatus(
                        IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus.FAILED.name()
                );

        IntraBankTransactionJobDetail intraBankTransactionJobDetail = IntraBankTransactionJobDetail.builder()
                .job(intraBankTransactionJob)
                .transaction(intraBankTransaction)
                .processedAt(LocalDateTime.now())
                .status(failedIntraBankTransactionJobStatus)
                .errorMessage(errorMessage)
                .build();

        repository.save(intraBankTransactionJobDetail);
        log.info("Failed job detail created with id: {}", intraBankTransactionJobDetail.getId());
    }
}
