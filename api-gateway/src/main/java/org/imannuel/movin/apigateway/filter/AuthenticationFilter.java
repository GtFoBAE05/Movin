package org.imannuel.movin.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imannuel.movin.commonservice.dto.response.template.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.apigateway.dto.request.AuthenticationValidationRequest;
import org.imannuel.movin.apigateway.dto.response.AuthenticationValidationResponse;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements GatewayFilter, Ordered {
    private static final String USER_ID_HEADER = "X-USER-ID";
    private static final String USER_ROLE_HEADER = "X-USER-ROLE";

    private final WebClient webClient;
    private final DiscoveryClient discoveryClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = extractToken(exchange.getRequest());
        if (token == null) {
            log.error("Bearer token not found");
            return onError(exchange, "Bearer token not found", HttpStatus.BAD_REQUEST);
        }

        log.debug("Extracted bearer token: {}", token);

        String serviceUrl = discoveryClient.getInstances("authentication-service")
                .stream()
                .findFirst()
                .map(instance -> {
                    String url = instance.getUri().toString();
                    log.info("Authentication service URL: {}", url);
                    return url;
                })
                .orElseThrow(() -> {
                    log.error("Authentication service not found");
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication service not found");
                });

        log.debug("Sending token validation request to: {}", serviceUrl + "/api/auth/validate-token");

        return webClient
                .post()
                .uri(serviceUrl + "/api/auth/validate-token")
                .bodyValue(new AuthenticationValidationRequest(token))
                .retrieve()
                .bodyToMono(AuthenticationValidationResponse.class)
                .flatMap(response -> {
                    log.info("Successfully validated token for user: {}", response.getUserId());
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header(USER_ID_HEADER, response.getUserId())
                            .header(USER_ROLE_HEADER, response.getRole())
                            .build();
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                .onErrorResume(throwable -> {
                    log.error("Error during token validation: {}", throwable.getMessage(), throwable);
                    if (throwable instanceof WebClientResponseException exception) {
                        return onError(exchange, throwable.getMessage(), HttpStatus.valueOf(exception.getStatusCode().value()));
                    }
                    return onError(exchange, throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.debug("Extracted bearer token from headers: {}", bearerToken);
        return parseToken(bearerToken);
    }

    private String parseToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        log.warn("Invalid or missing Bearer token");
        return null;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        log.info("Sending error response with status: {} and message: {}", status, message);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .success(false)
                .message(message)
                .data(null)
                .pagination(null)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        try {
            DataBuffer dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(commonResponse));
            return exchange.getResponse().writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            log.error("Error writing error response", e);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}