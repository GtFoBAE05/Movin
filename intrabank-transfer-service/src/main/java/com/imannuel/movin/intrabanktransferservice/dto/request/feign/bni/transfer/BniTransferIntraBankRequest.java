package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniTransferIntraBankRequest {
    private String partnerReferenceNo;

    private BniTransferIntraBankAmountRequest amount;

    private String beneficiaryAccountNo;

    private String beneficiaryEmail;

    private String customerReference;

    private String currency;

    private String remark;

    private String feeType;

    private String sourceAccountNo;

    private String transactionDate;

    private BniTransferIntraBankAdditionalInfoRequest additionalInfo;
}


