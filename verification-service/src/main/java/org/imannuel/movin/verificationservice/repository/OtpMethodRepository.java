package org.imannuel.movin.verificationservice.repository;

import org.imannuel.movin.verificationservice.enums.OtpMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpMethodRepository extends JpaRepository<OtpMethod, Integer> {
    Optional<OtpMethod> findByName(OtpMethod.Method name);

    boolean existsByName(OtpMethod.Method name);
}
