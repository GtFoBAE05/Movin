package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bni.balance;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.BalanceResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniGetBalanceResponse implements BalanceResponse {
    private String accountNo;

    private String partnerReferenceNo;

    private String responseMessage;

    private String responseCode;

    private BniGetBalanceInnerResponse additionalInfo;
}
