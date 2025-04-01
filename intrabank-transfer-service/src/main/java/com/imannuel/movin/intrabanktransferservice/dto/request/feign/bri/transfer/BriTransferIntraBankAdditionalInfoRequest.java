package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriTransferIntraBankAdditionalInfoRequest {
    private String deviceId;

    private String channel;
}
