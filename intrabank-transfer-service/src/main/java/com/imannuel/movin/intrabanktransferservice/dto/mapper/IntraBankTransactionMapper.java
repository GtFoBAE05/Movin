package com.imannuel.movin.intrabanktransferservice.dto.mapper;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.BankFeignResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.IntraBankTransactionResponse;
import com.imannuel.movin.intrabanktransferservice.entity.BankAccount;
import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransaction;

public class IntraBankTransactionMapper {
    public static IntraBankTransactionResponse toIntraBankTransactionResponse(
            IntraBankTransaction intraBankTransaction, BankFeignResponse senderbank, BankFeignResponse recipientBank,
            BankAccount recipientBankAccount
    ) {
        return IntraBankTransactionResponse.builder()
                .transactionId(intraBankTransaction.getId())
                .userId(intraBankTransaction.getSenderId())
                .amountToTransfer(intraBankTransaction.getAmount())
                .uniqueCode(intraBankTransaction.getUniqueCode())
                .amountToPaid(intraBankTransaction.getAmount() + intraBankTransaction.getUniqueCode())
                .recipientBank(BankMapper.toBankResponse(recipientBank))
                .recipientName(recipientBankAccount.getAccountHolder())
                .senderBank(BankMapper.toBankResponse(senderbank))
                .timeRequest(intraBankTransaction.getTimeRequest() == null ? "" : intraBankTransaction.getTimeRequest().toString())
                .timeServed(intraBankTransaction.getTimeServed() == null ? "" : intraBankTransaction.getTimeServed().toString())
                .status(intraBankTransaction.getStatus().getStatus().name())
                .build();
    }
}
