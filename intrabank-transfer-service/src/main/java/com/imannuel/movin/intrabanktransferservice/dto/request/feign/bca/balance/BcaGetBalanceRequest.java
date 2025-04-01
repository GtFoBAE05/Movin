package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaGetBalanceRequest {
    private String partnerReferenceNo;

    private String accountNo;
}
