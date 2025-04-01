package com.imannuel.movin.intrabanktransferservice.repository;

import com.imannuel.movin.intrabanktransferservice.enums.IntrabankTransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntraBankTransactionStatusRepository extends JpaRepository<IntrabankTransactionStatus, Integer> {
    boolean existsByStatus(IntrabankTransactionStatus.EIntraBankTransactionStatus status);

    Optional<IntrabankTransactionStatus> findByStatus(IntrabankTransactionStatus.EIntraBankTransactionStatus status);
}
