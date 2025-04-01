package com.imannuel.movin.intrabanktransferservice.repository;

import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransactionJobDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntraBankTransactionJobDetailRepository extends JpaRepository<IntraBankTransactionJobDetail, String> {
}
