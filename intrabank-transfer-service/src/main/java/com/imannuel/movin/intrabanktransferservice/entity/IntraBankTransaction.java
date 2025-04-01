package com.imannuel.movin.intrabanktransferservice.entity;

import com.imannuel.movin.intrabanktransferservice.enums.IntrabankTransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_intra_bank_transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntraBankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "sender_bank_id", nullable = false)
    private String senderBankId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "unique_code", nullable = false)
    private Long uniqueCode;

    @Column(name = "recipient_account_number", nullable = false)
    private String recipientAccountNumber;

    @Column(name = "recipient_bank_id", nullable = false)
    private String recipientBankId;

    @Column(name = "remark")
    private String remark;

    @Column(name = "time_request")
    private LocalDateTime timeRequest;

    @Column(name = "time_served")
    private LocalDateTime timeServed;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private IntrabankTransactionStatus status;
}
