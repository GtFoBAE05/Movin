package com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserFeignResponse {
    private String id;

    private String phoneNumber;

    private String fullName;

    private String email;

    private RoleFeignResponse role;
}
