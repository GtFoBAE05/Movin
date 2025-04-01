package com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniAuthorizationRequest {
    @JsonProperty("grantType")
    private String grantType;
}
