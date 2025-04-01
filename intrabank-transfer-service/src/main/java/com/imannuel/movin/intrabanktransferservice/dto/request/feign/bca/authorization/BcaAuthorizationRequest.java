package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaAuthorizationRequest {
    @JsonProperty("grantType")
    private String grantType;
}
