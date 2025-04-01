package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaTransferAdditionalInfo {
    private String economicActivity;

    private String transactionPurpose;

    private String beneficiaryEmail;
}
