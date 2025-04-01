package com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankFeignResponse {
    private String id;

    private String code;

    private String name;

    private Integer fee;

    private String image;

    private boolean status;
}
