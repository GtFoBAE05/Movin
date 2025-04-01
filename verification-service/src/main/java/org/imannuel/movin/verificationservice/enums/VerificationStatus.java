package org.imannuel.movin.verificationservice.enums;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "e_verification_status")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VerificationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status name;

    public enum Status {
        PENDING,
        USED,
        EXPIRED,
        ERROR
    }
}
