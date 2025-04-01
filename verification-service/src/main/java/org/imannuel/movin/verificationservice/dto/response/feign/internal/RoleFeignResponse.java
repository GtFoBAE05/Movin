package org.imannuel.movin.verificationservice.dto.response.feign.internal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleFeignResponse {
    private Integer id;

    private String name;
}
