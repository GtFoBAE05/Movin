package com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MandiriAuthorizationRequest {
    @JsonProperty("grantType")
    private String grantType;
}
