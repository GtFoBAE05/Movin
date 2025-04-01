package com.imannuel.movin.intrabanktransferservice.service;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.BankFeignResponse;

public interface BankFeignClientService {
    BankFeignResponse findBankByCode(String code);

    BankFeignResponse findBankById(String id);
}
