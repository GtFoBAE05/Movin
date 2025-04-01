package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bni.balance;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniGetBalanceInnerResponse {
    private String accountType;

    private String accountCurrency;

    private String accountBalance;

    private String customerName;

    private String status;
}
