package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniTransferIntraBankAmountRequest{
    private String value;

    private String currency;
}
