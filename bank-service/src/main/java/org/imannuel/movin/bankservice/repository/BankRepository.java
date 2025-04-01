package org.imannuel.movin.bankservice.repository;

import org.imannuel.movin.bankservice.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, String> {
    boolean existsByCode(String code);

    Optional<Bank> findByCode(String code);

    List<Bank> findAllByStatusTrue();

    List<Bank> findAllByStatusTrueAndCodeIsNot(String code);
}
