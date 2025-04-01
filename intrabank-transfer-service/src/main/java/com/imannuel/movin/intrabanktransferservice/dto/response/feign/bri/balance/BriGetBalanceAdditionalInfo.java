package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriGetBalanceAdditionalInfo {
    private String productCode;

    private String accountType;
}
