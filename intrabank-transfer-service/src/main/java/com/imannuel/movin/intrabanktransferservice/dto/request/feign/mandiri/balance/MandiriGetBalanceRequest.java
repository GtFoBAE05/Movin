package com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriGetBalanceRequest {
    private String accountNo;
}
