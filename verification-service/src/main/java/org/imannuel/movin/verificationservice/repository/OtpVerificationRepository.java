package org.imannuel.movin.verificationservice.repository;

import org.imannuel.movin.verificationservice.entity.OtpVerification;
import org.imannuel.movin.verificationservice.enums.OtpMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, String> {
    Optional<OtpVerification> findByUserIdAndOtpMethodAndVerificationCode(String userId, OtpMethod otpMethod, String verificationCode);

    Integer countAllByUserIdAndVerificationSentAtAfter(String userId, LocalDateTime verificationSentAtAfter);
}
