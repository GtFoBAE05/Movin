package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.account_inquiry;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaInternalAccountInquiryRequest {
    private String partnerReferenceNo;

    private String beneficiaryAccountNo;
}
