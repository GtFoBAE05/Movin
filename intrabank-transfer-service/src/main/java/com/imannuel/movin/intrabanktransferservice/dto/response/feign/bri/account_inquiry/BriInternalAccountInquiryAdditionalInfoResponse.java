package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.account_inquiry;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriInternalAccountInquiryAdditionalInfoResponse {
    private String deviceId;

    private String channel;
}
