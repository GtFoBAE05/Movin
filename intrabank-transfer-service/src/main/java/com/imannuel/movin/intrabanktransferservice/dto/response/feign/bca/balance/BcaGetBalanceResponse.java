package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.balance;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.BalanceResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaGetBalanceResponse implements BalanceResponse {
    private String responseCode;

    private String responseMessage;

    private String referenceNo;

    private String partnerReferenceNo;

    private String accountNo;

    private String name;

    private BcaGetBalanceAccountInfos accountInfos;
}


