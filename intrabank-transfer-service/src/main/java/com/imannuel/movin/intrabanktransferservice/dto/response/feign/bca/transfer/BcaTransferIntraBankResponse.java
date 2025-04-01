package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.transfer;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.IntraBankTransferResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaTransferIntraBankResponse implements IntraBankTransferResponse {
    private String responseCode;

    private String responseMessage;

    private String referenceNo;

    private String partnerReferenceNo;

    private BcaTransferAmountResponse amount;

    private String beneficiaryAccountNo;

    private String sourceAccountNo;

    private String transactionDate;

    private String currency;

    private String customerReference;

    private BcaTransferAdditionalInfo additionalInfo;

    @Override
    public boolean isSuccessfully() {
        return responseCode.startsWith("200");
    }

    @Override
    public String getMessage() {
        return responseMessage;
    }
}
