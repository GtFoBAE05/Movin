package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bni.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniTransferIntraBankAmountResponse {
    private String value;

    private String currency;
}