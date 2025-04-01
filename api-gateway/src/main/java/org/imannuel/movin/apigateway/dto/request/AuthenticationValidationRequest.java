package org.imannuel.movin.apigateway.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationValidationRequest {
    private String token;
}
