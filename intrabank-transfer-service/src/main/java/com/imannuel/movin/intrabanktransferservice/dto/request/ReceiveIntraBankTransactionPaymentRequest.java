package com.imannuel.movin.intrabanktransferservice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiveIntraBankTransactionPaymentRequest {
    private String transactionId;
}
