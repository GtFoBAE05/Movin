package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriAuthorizationRequest {
    @JsonProperty("grantType")
    private String grantType;
}
