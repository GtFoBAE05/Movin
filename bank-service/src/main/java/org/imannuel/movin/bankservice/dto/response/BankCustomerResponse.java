package org.imannuel.movin.bankservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankCustomerResponse {
    private String code;

    private String name;

    private String imageUrl;
}
