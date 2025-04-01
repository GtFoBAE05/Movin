package com.imannuel.movin.intrabanktransferservice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountInquiryRequest {
    private String bankCode;

    private String accountNumber;
}
