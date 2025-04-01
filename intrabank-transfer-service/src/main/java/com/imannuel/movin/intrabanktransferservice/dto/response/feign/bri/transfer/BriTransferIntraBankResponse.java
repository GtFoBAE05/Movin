package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.transfer;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.IntraBankTransferResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriTransferIntraBankResponse implements IntraBankTransferResponse {
    private String responseCode;

    private String responseMessage;

    private String referenceNo;

    private String partnerReferenceNo;

    private BriTransferIntraBankAmountResponse amount;

    private String beneficiaryAccountNo;

    private String customerReference;

    private String sourceAccountNo;

    private String transactionDate;

    private BriTransferIntraBankAdditionalInfoResponse additionalInfo;

    @Override
    public boolean isSuccessfully() {
        return responseCode.startsWith("200");
    }

    @Override
    public String getMessage() {
        return responseMessage;
    }
}