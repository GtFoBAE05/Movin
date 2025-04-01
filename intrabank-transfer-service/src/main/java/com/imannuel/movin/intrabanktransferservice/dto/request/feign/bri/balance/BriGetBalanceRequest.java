package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriGetBalanceRequest {
    private String accountNo;
}
