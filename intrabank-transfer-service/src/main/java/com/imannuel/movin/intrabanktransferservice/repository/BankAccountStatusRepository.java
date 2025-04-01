package com.imannuel.movin.intrabanktransferservice.repository;

import com.imannuel.movin.intrabanktransferservice.enums.BankAccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountStatusRepository extends JpaRepository<BankAccountStatus, Integer> {
    boolean existsByStatus(BankAccountStatus.EBankAccountStatus name);

    Optional<BankAccountStatus> findByStatus(BankAccountStatus.EBankAccountStatus status);
}
