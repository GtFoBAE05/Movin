package com.imannuel.movin.authenticationservice.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.UserFeignResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
    String generateToken(UserFeignResponse user);

    String extractSubject(String token);

    String extractTokenFromRequest(HttpServletRequest httpServletRequest);

    DecodedJWT extractClaimJWT(String token);

    String parseToken(String bearerToken);
}
