package org.imannuel.movin.verificationservice.repository;

import org.imannuel.movin.verificationservice.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationStatusRepository extends JpaRepository<VerificationStatus, Integer> {
    Optional<VerificationStatus> findByName(VerificationStatus.Status name);

    boolean existsByName(VerificationStatus.Status name);
}
