package com.imannuel.movin.intrabanktransferservice.repository;

import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransaction;
import com.imannuel.movin.intrabanktransferservice.enums.IntrabankTransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntraBankTransactionRepository extends JpaRepository<IntraBankTransaction, String> {
    List<IntraBankTransaction> findAllByStatus(IntrabankTransactionStatus status);
}
