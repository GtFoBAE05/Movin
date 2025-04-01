package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bni.account_inquiry;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniInternalAccountInquiryAdditionalInfoResponse {
    private String deviceId;

    private String channel;
}
