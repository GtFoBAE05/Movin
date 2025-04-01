package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.authorization;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AuthorizationResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriAuthorizationResponse implements AuthorizationResponse {
    private String accessToken;

    private String tokenType;

    private Long expiresIn;
}