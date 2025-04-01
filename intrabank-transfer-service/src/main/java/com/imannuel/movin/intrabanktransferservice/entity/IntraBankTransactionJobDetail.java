package com.imannuel.movin.intrabanktransferservice.entity;

import com.imannuel.movin.intrabanktransferservice.enums.IntraBankTransactionJobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_intra_bank_transaction_job_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntraBankTransactionJobDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private IntraBankTransactionJob job;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private IntraBankTransaction transaction;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private IntraBankTransactionJobStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;
}
