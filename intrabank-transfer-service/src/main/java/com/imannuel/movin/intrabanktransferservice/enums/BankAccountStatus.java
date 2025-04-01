package com.imannuel.movin.intrabanktransferservice.enums;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_bank_account_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EBankAccountStatus status;

    public enum EBankAccountStatus {
        PENDING,
        SUCCESS,
        INVALID_ACCOUNT_NUMBER,
        SUSPECTED_ACCOUNT,
        BLACK_LISTED
    }
}
