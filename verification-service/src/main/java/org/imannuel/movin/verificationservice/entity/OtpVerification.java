package org.imannuel.movin.verificationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.imannuel.movin.verificationservice.enums.OtpMethod;
import org.imannuel.movin.verificationservice.enums.VerificationStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "m_otp_verifications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OtpVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "otp_method_id", nullable = false)
    private OtpMethod otpMethod;

    @Column(name = "verification_code", nullable = false)
    private String verificationCode;

    @Column(name = "verification_sent_at", nullable = false)
    private LocalDateTime verificationSentAt;

    @Column(name = "verification_expires_at", nullable = false)
    private LocalDateTime verificationExpiresAt;

    @ManyToOne
    @JoinColumn(name = "verification_status_id", nullable = false)
    private VerificationStatus verificationStatus;

    @Column(name = "error_message")
    private String errorMessage;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.verificationExpiresAt);
    }
}
