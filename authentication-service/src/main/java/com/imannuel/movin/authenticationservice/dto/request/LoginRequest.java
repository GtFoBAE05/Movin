package com.imannuel.movin.authenticationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "identifier is required")
    private String identifier;

    @NotBlank(message = "credential is required")
    private String credential;
}
