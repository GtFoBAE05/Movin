package com.imannuel.movin.authenticationservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.UserFeignResponse;
import com.imannuel.movin.authenticationservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${movin.jwt-secret}")
    private String SECRET_KEY;

    @Value("${movin.jwt-access-token-expiration-in-minutes-access-token}")
    private Long EXPIRATION_IN_MINUTES;

    @Value("${movin.jwt-issuer}")
    private String ISSUER;

    @Override
    public String generateToken(UserFeignResponse user) {
        log.info("Generating JWT token for user: {}", user.getId());
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            String token = JWT.create()
                    .withExpiresAt(Instant.now().plus(EXPIRATION_IN_MINUTES, ChronoUnit.MINUTES))
                    .withIssuer(ISSUER)
                    .withSubject(user.getId())
                    .withClaim("role", user.getRole().getName())
                    .sign(algorithm);
            log.debug("JWT token generated successfully for user: {}", user.getId());
            return token;
        } catch (JWTCreationException e) {
            log.error("Error occurred while generating JWT token: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error when creating JWT");
        }
    }

    @Override
    public String extractSubject(String token) {
        log.info("Extracting subject from JWT token {}", token);
        DecodedJWT decodedJWT = extractClaimJWT(token);
        if (decodedJWT != null) {
            String subject = decodedJWT.getSubject();
            log.debug("Subject extracted from JWT token: {}", subject);
            return subject;
        }
        log.warn("No subject found in JWT token");
        return null;
    }

    @Override
    public String extractTokenFromRequest(HttpServletRequest httpServletRequest) {
        log.info("Extracting JWT token from request");
        String bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = parseToken(bearerToken);
        log.debug("JWT token extracted from request: {}", token);
        return token;
    }

    @Override
    public DecodedJWT extractClaimJWT(String token) {
        log.info("Extracting claims from JWT token {}", token);
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            log.debug("Claims extracted successfully from JWT token {}", token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            log.error("Error occurred while verifying JWT token: {}", exception.getMessage(), exception);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, exception.getMessage());
        }
    }

    @Override
    public String parseToken(String bearerToken) {
        log.debug("Parsing bearer token {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.debug("Bearer token {} parsed successfully", bearerToken);
            return token;
        }
        log.warn("Invalid or missing Bearer token");
        return null;
    }
}