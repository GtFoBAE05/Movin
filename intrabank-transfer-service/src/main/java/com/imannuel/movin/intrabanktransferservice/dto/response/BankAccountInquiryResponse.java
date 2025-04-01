package com.imannuel.movin.intrabanktransferservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountInquiryResponse {
    private String bankCode;

    private String accountNumber;

    private String accountHolder;

    private String status;
}
