package org.imannuel.movin.userservice.repository;

import org.imannuel.movin.userservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    boolean existsByReferralCode(String referralCode);
}
