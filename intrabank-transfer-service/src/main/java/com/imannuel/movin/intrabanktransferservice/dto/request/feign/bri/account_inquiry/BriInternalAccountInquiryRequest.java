package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.account_inquiry;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriInternalAccountInquiryRequest {
    private String beneficiaryAccountNo;

    private BriInternalAccountInquiryAdditionalInfoRequest additionalInfo;
}
