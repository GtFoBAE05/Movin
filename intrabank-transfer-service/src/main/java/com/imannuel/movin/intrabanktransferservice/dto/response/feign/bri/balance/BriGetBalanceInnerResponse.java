package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriGetBalanceInnerResponse {
    private BriGetBalanceAmountResponse holdAmount;

    private BriGetBalanceAmountResponse availableBalance;

    private BriGetBalanceAmountResponse ledgerBalance;

    private String status;
}