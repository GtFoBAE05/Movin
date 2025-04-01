package com.imannuel.movin.intrabanktransferservice.dto.request.feign;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorizationRequest {
    @JsonProperty("grantType")
    private String grantType;
}
