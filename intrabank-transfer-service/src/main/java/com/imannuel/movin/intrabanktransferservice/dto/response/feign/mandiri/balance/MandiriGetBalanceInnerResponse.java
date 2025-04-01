package com.imannuel.movin.intrabanktransferservice.dto.response.feign.mandiri.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriGetBalanceInnerResponse {
    private MandiriGetBalanceAmountResponse amount;

    private MandiriGetBalanceAmountResponse availableBalance;

    private MandiriGetBalanceAmountResponse ledgerBalance;

    private String status;
}
