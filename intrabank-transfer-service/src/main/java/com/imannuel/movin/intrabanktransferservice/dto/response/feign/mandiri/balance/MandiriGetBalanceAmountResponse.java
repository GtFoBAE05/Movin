package com.imannuel.movin.intrabanktransferservice.dto.response.feign.mandiri.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriGetBalanceAmountResponse {
    private String value;

    private String currency;
}
