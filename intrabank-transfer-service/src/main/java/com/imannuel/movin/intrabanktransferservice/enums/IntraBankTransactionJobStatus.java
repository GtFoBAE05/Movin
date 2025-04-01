package com.imannuel.movin.intrabanktransferservice.enums;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_intra_bank_transaction_job_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntraBankTransactionJobStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntraBankTransactionJobStatus.EIntraBankTransactionJobStatus status;

    public enum EIntraBankTransactionJobStatus {
        STARTED,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
    }
}
