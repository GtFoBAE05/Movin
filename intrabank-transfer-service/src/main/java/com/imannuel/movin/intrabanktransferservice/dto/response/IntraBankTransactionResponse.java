package com.imannuel.movin.intrabanktransferservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntraBankTransactionResponse {
    private String transactionId;

    private String userId;

    private Long amountToTransfer;

    private Long uniqueCode;

    private Long amountToPaid;

    private BankResponse recipientBank;

    private String recipientAccountNumber;

    private String recipientName;

    private BankResponse senderBank;

    private String timeRequest;

    private String timeServed;

    private String status;
}
