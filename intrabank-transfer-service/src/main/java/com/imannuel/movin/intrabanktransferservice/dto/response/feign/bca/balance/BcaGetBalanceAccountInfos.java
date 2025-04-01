package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaGetBalanceAccountInfos {
    private String balanceType;

    private BcaGetBalanceAmountResponse amount;

    private BcaGetBalanceAmountResponse floatAmount;

    private BcaGetBalanceAmountResponse holdAmount;

    private BcaGetBalanceAmountResponse availableBalance;

    private BcaGetBalanceAmountResponse ledgerBalance;

    private BcaGetBalanceAmountResponse currentMultilateralLimit;

    private String registrationStatusCode;

    private String status;
}
