package com.imannuel.movin.intrabanktransferservice.dto.mapper;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.BankFeignResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.BankAccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.entity.BankAccount;

public class BankAccountMapper {
    public static BankAccountInquiryResponse toBankAccountInquiryResponse(BankFeignResponse bank, BankAccount bankAccount) {
        return BankAccountInquiryResponse.builder()
                .bankCode(bank.getCode())
                .accountNumber(bankAccount.getAccountNumber())
                .accountHolder(bankAccount.getAccountHolder())
                .status(bankAccount.getBankAccountStatus().getStatus().name())
                .build();
    }
}
