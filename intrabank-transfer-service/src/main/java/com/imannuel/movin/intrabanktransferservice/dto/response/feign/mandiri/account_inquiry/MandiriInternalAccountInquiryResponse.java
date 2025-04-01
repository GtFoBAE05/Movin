package com.imannuel.movin.intrabanktransferservice.dto.response.feign.mandiri.account_inquiry;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AccountInquiryResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriInternalAccountInquiryResponse implements AccountInquiryResponse {
    private String responseCode;

    private String responseMessage;

    private String referenceNo;

    private String beneficiaryAccountName;

    private String beneficiaryAccountNo;

    private String beneficiaryAccountStatus;

    private String beneficiaryAccountType;

    private String currency;

    @Override
    public String getAccountHolderName() {
        return beneficiaryAccountName;
    }

    @Override
    public String getAccountNumber() {
        return beneficiaryAccountNo;
    }
}
