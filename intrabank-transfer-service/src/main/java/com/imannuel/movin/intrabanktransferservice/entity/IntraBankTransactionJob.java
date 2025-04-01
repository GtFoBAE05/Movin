package com.imannuel.movin.intrabanktransferservice.entity;

import com.imannuel.movin.intrabanktransferservice.enums.IntraBankTransactionJobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_intra_bank_transaction_jobs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntraBankTransactionJob {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private IntraBankTransactionJobStatus status;

    @Column(name = "total_transactions")
    private Integer totalTransactions;

    @Column(name = "successful_count")
    private Integer successfulCount;

    @Column(name = "failed_count")
    private Integer failedCount;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "error_message")
    private String errorMessage;
}
