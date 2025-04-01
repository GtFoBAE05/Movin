package org.imannuel.movin.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        System.out.println("Eureka URL: " + System.getenv("EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE"));
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
