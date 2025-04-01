package com.imannuel.movin.intrabanktransferservice.dto.mapper;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.BankFeignResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.BankResponse;

public class BankMapper {
    public static BankResponse toBankResponse(BankFeignResponse bank) {
        return BankResponse.builder()
                .code(bank.getCode())
                .name(bank.getName())
                .imageUrl(bank.getImage())
                .build();
    }

}
