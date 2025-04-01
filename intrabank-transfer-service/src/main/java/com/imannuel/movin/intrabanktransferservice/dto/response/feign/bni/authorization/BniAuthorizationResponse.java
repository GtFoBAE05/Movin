package com.imannuel.movin.intrabanktransferservice.dto.response.feign.bni.authorization;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AuthorizationResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BniAuthorizationResponse implements AuthorizationResponse {
    private String responseCode;

    private String responseMessage;

    private String accessToken;

    private String tokenType;

    private Long expiresIn;

    private Object additionalInfo;
}
