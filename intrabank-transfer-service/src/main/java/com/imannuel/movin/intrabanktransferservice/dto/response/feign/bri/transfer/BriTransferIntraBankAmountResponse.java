package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriTransferIntraBankAmountResponse {
    private String value;

    private String currency;
}