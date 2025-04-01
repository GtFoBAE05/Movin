package org.imannuel.movin.verificationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.imannuel.movin.verificationservice.enums.VerificationStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "m_identity_verification_histories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IdentityVerificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Lob
    @Column(name = "ktp_image", nullable = false)
    private byte[] ktpImage;

    @Lob
    @Column(name = "selfie_image", nullable = false)
    private byte[] selfieImage;

    @Column(name = "verification_request_at", nullable = false)
    private LocalDateTime verificationRequestAt;

    @Column(name = "verification_verified_at")
    private LocalDateTime verificationVerifiedAt;

    @ManyToOne
    @JoinColumn(name = "verification_status_id", nullable = false)
    private VerificationStatus verificationStatus;

    @Column(name = "error_message")
    private String errorMessage;
}
