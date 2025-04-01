package com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriTransferIntraBankRequest {
    private String partnerReferenceNo;

    private MandiriTransferIntraBankAmountRequest amount;

    private String sourceAccountNo;

    private String beneficiaryAccountNo;

    private String remark;

    private String transactionDate;

    private String beneficiaryEmail;

    private MandiriTransferIntraBankAdditionalInfoRequest additionalInfo;
}


