package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransactionJob;
import com.imannuel.movin.intrabanktransferservice.enums.IntraBankTransactionJobStatus;
import com.imannuel.movin.intrabanktransferservice.repository.IntraBankTransactionJobRepository;
import com.imannuel.movin.intrabanktransferservice.service.IntraBankTransactionJobService;
import com.imannuel.movin.intrabanktransferservice.service.IntraBankTransactionJobStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntraBankTransactionJobServiceImpl implements IntraBankTransactionJobService {
    private final IntraBankTransactionJobRepository repository;
    private final IntraBankTransactionJobStatusService intraBankTransactionJobStatusService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IntraBankTransactionJob createIntraBankTransactionJob() {
        log.info("Creating a new intra-bank transaction job");

        IntraBankTransactionJobStatus intraBankTransactionJobStatus = intraBankTransactionJobStatusService.findIntraBankTransactionJobStatus(
                IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus.STARTED.name()
        );

        IntraBankTransactionJob transactionJob = IntraBankTransactionJob.builder()
                .status(intraBankTransactionJobStatus)
                .startTime(LocalDateTime.now())
                .successfulCount(0)
                .failedCount(0)
                .build();
        repository.saveAndFlush(transactionJob);

        log.info("Created intra-bank transaction job with id: {}", transactionJob.getId());
        return transactionJob;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishIntraBankTransactionJob(IntraBankTransactionJob intraBankTransactionJob) {
        log.info("Finishing intra-bank transaction job with id: {}", intraBankTransactionJob.getId());

        IntraBankTransactionJobStatus intraBankTransactionJobStatus = intraBankTransactionJobStatusService.findIntraBankTransactionJobStatus(
                IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus.COMPLETED.name()
        );

        intraBankTransactionJob.setStatus(intraBankTransactionJobStatus);
        intraBankTransactionJob.setEndTime(LocalDateTime.now());
        repository.saveAndFlush(intraBankTransactionJob);

        log.info("Finished intra-bank transaction job with id: {}, status: {}", intraBankTransactionJob.getId(), intraBankTransactionJob.getStatus().getStatus());
    }
}
