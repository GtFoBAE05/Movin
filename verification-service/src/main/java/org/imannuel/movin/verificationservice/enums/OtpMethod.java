package org.imannuel.movin.verificationservice.enums;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "e_otp_method")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OtpMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Method name;

    public enum Method {
        SMS,
        WHATSAPP,
        EMAIL
    }
}
