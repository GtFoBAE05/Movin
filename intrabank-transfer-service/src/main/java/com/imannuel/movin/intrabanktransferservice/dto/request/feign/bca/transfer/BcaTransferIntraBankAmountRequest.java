package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaTransferIntraBankAmountRequest {
    private String value;

    private String currency;

    private String dateTime;
}
