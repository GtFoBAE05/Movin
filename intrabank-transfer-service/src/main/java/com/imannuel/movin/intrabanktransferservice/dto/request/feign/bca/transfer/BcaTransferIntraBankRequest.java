package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaTransferIntraBankRequest {
    private String partnerReferenceNo;

    private BcaTransferIntraBankAmountRequest amount;

    private String beneficiaryAccountNo;

    private String beneficiaryAccountName;

    private String beneficiaryEmail;

    private String remark;

    private String sourceAccountNo;

    private String transactionDate;

    private BcaTransferIntraBankAdditionalInfoRequest additionalInfo;
}
