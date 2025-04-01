package com.imannuel.movin.intrabanktransferservice.entity;

import com.imannuel.movin.intrabanktransferservice.enums.BankAccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "m_bank_accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(name = "bank_id")
    private String bankId;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "account_holder", nullable = false)
    private String accountHolder;

    @ManyToOne
    @JoinColumn(name = "bank_account_status_id")
    private BankAccountStatus bankAccountStatus;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;
}
