package org.imannuel.movin.bankservice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentOptionsRequest {
    private String targetBank;
}
