package com.imannuel.movin.intrabanktransferservice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateIntraBankTransactionRequest {
    private String recipientAccountNumber;

    private String recipientBankCode;

    private String senderBankCode;

    private Long amount;

    private String remark;
}
