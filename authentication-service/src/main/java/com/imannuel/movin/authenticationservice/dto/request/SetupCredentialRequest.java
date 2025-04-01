package com.imannuel.movin.authenticationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetupCredentialRequest {
    @NotBlank(message = "identifier is required")
    private String identifier;

    @NotBlank(message = "credential is required")
    @Size(min = 6, max = 6, message = "credential should have 6 characters")
    private String credential;
}
