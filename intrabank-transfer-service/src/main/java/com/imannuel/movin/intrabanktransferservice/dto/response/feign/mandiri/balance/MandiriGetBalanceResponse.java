package com.imannuel.movin.intrabanktransferservice.dto.response.feign.mandiri.balance;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.BalanceResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriGetBalanceResponse implements BalanceResponse {
    private String responseCode;

    private String responseMessage;

    private String accountNo;

    private String name;

    private MandiriGetBalanceInnerResponse accountInfos;
}
