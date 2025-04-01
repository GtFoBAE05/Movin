package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniTransferIntraBankAdditionalInfoRequest {
    private String deviceId;

    private String channel;
}
