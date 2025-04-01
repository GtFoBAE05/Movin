package com.imannuel.movin.intrabanktransferservice.repository;

import com.imannuel.movin.intrabanktransferservice.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    Optional<BankAccount> findByBankIdAndAccountNumber(String bankId, String accountNumber);
}
