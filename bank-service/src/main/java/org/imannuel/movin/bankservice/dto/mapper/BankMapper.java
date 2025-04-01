package org.imannuel.movin.bankservice.dto.mapper;

import org.imannuel.movin.bankservice.dto.request.BankRequest;
import org.imannuel.movin.bankservice.dto.response.BankCustomerResponse;
import org.imannuel.movin.bankservice.dto.response.BankResponse;
import org.imannuel.movin.bankservice.entity.Bank;

public class BankMapper {
    public static Bank toBank(BankRequest bankRequest) {
        return Bank.builder()
                .code(bankRequest.getCode())
                .name(bankRequest.getName())
                .fee(bankRequest.getFee())
                .image(bankRequest.getImage())
                .status(bankRequest.isStatus())
                .build();
    }

    public static BankCustomerResponse toBankCustomerResponse(Bank bank) {
        return BankCustomerResponse.builder()
                .code(bank.getCode())
                .name(bank.getName())
                .imageUrl(bank.getImage())
                .build();
    }

    public static BankResponse toBankResponse(Bank bank) {
        return BankResponse.builder()
                .id(bank.getId())
                .code(bank.getCode())
                .name(bank.getName())
                .fee(bank.getFee())
                .imageUrl(bank.getImage())
                .status(bank.isStatus())
                .build();
    }
}
