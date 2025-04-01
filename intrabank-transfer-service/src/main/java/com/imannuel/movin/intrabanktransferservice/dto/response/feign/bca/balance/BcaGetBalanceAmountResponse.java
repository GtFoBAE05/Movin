package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaGetBalanceAmountResponse {
    private String value;

    private String currency;
}
