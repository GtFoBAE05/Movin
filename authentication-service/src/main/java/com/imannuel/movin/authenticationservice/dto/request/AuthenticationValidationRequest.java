package com.imannuel.movin.authenticationservice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationValidationRequest {
    private String token;
}
