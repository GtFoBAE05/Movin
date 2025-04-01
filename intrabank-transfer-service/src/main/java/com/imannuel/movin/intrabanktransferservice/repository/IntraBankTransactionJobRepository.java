package com.imannuel.movin.intrabanktransferservice.repository;

import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransactionJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntraBankTransactionJobRepository extends JpaRepository<IntraBankTransactionJob, String> {
}
