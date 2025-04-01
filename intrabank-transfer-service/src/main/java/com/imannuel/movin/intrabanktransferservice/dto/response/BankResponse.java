package com.imannuel.movin.intrabanktransferservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankResponse {
    private String code;

    private String name;

    private String imageUrl;
}
