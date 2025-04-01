package com.imannuel.movin.intrabanktransferservice.dto.response.feign.mandiri.transfer;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.IntraBankTransferResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriTransferIntraBankResponse implements IntraBankTransferResponse {
    private String responseCode;

    private String responseMessage;

    private String sourceAccountNo;

    private String beneficiaryAccountNo;

    private MandiriTransferIntraBankAmountResponse amount;

    private String referenceNo;

    private String partnerReferenceNo;

    private String transactionDate;

    @Override
    public boolean isSuccessfully() {
        return responseCode.startsWith("200");
    }

    @Override
    public String getMessage() {
        return responseMessage;
    }
}
