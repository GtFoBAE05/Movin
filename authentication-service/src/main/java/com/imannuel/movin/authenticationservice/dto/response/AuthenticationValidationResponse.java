package com.imannuel.movin.authenticationservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationValidationResponse {
    private String userId;

    private String role;
}
