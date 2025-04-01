package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.balance;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.BalanceResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriGetBalanceResponse implements BalanceResponse {
    private String responseCode;

    private String responseMessage;

    private String accountNo;

    private String name;

    private List<BriGetBalanceInnerResponse> accountInfos;

    private BriGetBalanceAdditionalInfo additionalInfo;
}
