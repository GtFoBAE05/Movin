package com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriTransferIntraBankAmountRequest {
    private String value;

    private String currency;
}
