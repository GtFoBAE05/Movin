package com.imannuel.movin.intrabanktransferservice.dto.response.feign.mandiri.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriTransferIntraBankAmountResponse {
    private String value;

    private String currency;
}
