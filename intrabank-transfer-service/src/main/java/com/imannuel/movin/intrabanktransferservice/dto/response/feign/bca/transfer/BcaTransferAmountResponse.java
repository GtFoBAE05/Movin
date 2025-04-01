package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaTransferAmountResponse {
    private String value;

    private String currency;
}
