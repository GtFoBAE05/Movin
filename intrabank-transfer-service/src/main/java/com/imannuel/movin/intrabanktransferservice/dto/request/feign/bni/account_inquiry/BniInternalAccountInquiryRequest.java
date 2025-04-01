package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.account_inquiry;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniInternalAccountInquiryRequest {
    private String partnerReferenceNo;

    private String beneficiaryAccountNo;
}
