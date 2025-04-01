package org.imannuel.movin.apigateway.configuration;

import lombok.RequiredArgsConstructor;
import org.imannuel.movin.apigateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringCloudConfiguration {
    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/auth/**")
                        .uri("lb://authentication-service"))
                .route(r -> r.path("/api/banks/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authenticationFilter))
                        .uri("lb://bank-service"))
                .route(r -> r.path("/api/intra-bank/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authenticationFilter))
                        .uri("lb://intrabank-transfer-service"))
                .route(r -> r.path("/api/users/**")
                        .uri("lb://user-service"))
                .route(r -> r.path("/api/verification/**")
                        .uri("lb://verification-service"))
                .build();
    }
}
