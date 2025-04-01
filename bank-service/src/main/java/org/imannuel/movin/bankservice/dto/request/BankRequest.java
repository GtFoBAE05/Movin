package org.imannuel.movin.bankservice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankRequest {
    private String code;

    private String name;

    private Integer fee;

    private String image;

    private boolean status;
}
