package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaTransferIntraBankAdditionalInfoRequest {
    private String economicActivity;

    private String transactionPurpose;
}
