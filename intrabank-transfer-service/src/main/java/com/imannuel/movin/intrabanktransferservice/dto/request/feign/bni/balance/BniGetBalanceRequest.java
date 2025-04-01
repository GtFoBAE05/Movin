package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniGetBalanceRequest {
    private String partnerReferenceNo;

    private String accountNo;
}
