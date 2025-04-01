package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriTransferIntraBankAdditionalInfoResponse {
    private String deviceId;

    private String channel;
}
