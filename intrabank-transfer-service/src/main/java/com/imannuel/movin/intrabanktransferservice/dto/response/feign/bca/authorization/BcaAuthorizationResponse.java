package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.authorization;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AuthorizationResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BcaAuthorizationResponse implements AuthorizationResponse {
    private String responseCode;

    private String responseMessage;

    private String accessToken;

    private String tokenType;

    private Long expiresIn;

}
