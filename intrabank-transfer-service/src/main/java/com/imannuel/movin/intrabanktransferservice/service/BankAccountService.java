package com.imannuel.movin.intrabanktransferservice.service;

import com.imannuel.movin.intrabanktransferservice.dto.request.BankAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.BankAccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.BankFeignResponse;
import com.imannuel.movin.intrabanktransferservice.entity.BankAccount;

public interface BankAccountService {
    BankAccount newBankAccountInquiry(BankFeignResponse bank, String accountNumber);

    BankAccount getBankAccountInquiry(BankFeignResponse bank, String accountNumber);

    BankAccountInquiryResponse getBankAccountInquiry(BankAccountInquiryRequest bankAccountInquiryRequest);
}
