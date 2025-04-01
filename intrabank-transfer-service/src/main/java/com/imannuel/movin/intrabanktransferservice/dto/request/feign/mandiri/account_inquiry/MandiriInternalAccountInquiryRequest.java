package com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.account_inquiry;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriInternalAccountInquiryRequest {
    private String accountNo;
}
