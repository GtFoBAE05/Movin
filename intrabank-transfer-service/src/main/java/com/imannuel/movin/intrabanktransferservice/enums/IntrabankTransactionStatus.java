package com.imannuel.movin.intrabanktransferservice.enums;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_intra_bank_transaction_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntrabankTransactionStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EIntraBankTransactionStatus status;

    public enum EIntraBankTransactionStatus {
        WAITING_PAYMENT,
        PENDING,
        ON_PROGRESS,
        SUCCESS,
        FAILED,
        EXPIRED
    }
}
