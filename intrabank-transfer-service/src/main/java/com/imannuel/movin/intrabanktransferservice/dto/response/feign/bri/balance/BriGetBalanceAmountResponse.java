package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriGetBalanceAmountResponse {
    private String value;

    private String currency;
}
