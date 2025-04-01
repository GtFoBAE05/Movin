package com.imannuel.movin.intrabanktransferservice.dto.response.feign;

public interface AccountInquiryResponse {
    String getAccountHolderName();

    String getAccountNumber();
}
