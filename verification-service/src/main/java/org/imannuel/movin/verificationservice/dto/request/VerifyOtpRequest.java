package org.imannuel.movin.verificationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyOtpRequest {
    @NotBlank(message = "identifier is required")
    private String identifier;

    @NotBlank(message = "otp is required")
    private String otp;

    @NotBlank(message = "method is required")
    private String method;
}
