package com.imannuel.movin.intrabanktransferservice.dto.response.feign;

public interface AuthorizationResponse {
    String getAccessToken();

    String getTokenType();

    Long getExpiresIn();
}
