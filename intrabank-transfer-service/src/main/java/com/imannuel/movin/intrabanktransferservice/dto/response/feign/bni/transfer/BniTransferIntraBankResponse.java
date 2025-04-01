package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bni.transfer;

import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.transfer.BniTransferIntraBankAdditionalInfoRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.IntraBankTransferResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniTransferIntraBankResponse implements IntraBankTransferResponse {
    private String responseCode;

    private String responseMessage;

    private String referenceNo;

    private String partnerReferenceNo;

    private BniTransferIntraBankAmountResponse amount;

    private String beneficiaryAccountNo;

    private String currency;

    private String customerReference;

    private String sourceAccountNo;

    private String transactionDate;

    private BniTransferIntraBankAdditionalInfoRequest additionalInfo;

    @Override
    public boolean isSuccessfully() {
        return responseCode.startsWith("200");
    }

    @Override
    public String getMessage() {
        return responseMessage;
    }
}