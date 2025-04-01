package com.imannuel.movin.intrabanktransferservice.repository;

import com.imannuel.movin.intrabanktransferservice.enums.IntraBankTransactionJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntraBankTransactionJobStatusRepository extends JpaRepository<IntraBankTransactionJobStatus, String> {
    boolean existsByStatus(IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus status);

    Optional<IntraBankTransactionJobStatus> findByStatus(IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus status);

}
