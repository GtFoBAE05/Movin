package com.imannuel.movin.intrabanktransferservice.service;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AuthorizationResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.BalanceResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.IntraBankTransferResponse;

public interface BankService {
    AuthorizationResponse newAuthorizationToken();

    String getAuthorizationToken();

    BalanceResponse getCurrentBalance();

    AccountInquiryResponse getAccountInquiry(String accountNumber);

    IntraBankTransferResponse processIntrabankTransfer(String accountNumber, Long amount);
}
