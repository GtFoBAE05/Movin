package com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriTransferIntraBankAdditionalInfoRequest {
    private String reportCode;

    private String senderInstrument;

    private String senderAccountNo;

    private String senderName;

    private String senderCountry;

    private String senderCustomerType;

    private String beneficiaryAccountName;

    private String beneficiaryInstrument;

    private String beneficiaryCustomerType;
}
