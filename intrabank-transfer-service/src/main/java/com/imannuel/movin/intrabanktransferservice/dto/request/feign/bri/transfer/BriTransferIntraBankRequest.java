package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriTransferIntraBankRequest {
    private String partnerReferenceNo;

    private BriTransferIntraBankAmountRequest amount;

    private String beneficiaryAccountNo;

    private String customerReference;

    private String remark;

    private String feeType;

    private String sourceAccountNo;

    private String transactionDate;

    private BriTransferIntraBankAdditionalInfoRequest additionalInfo;
}


