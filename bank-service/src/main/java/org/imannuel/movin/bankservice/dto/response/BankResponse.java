package org.imannuel.movin.bankservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankResponse {
    private String id;

    private String code;

    private String name;

    private Integer fee;

    private String imageUrl;

    private boolean status;
}
