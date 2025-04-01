package org.imannuel.movin.verificationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendOtpRequest {
    @NotBlank(message = "method is required")
    private String method;

    @NotBlank(message = "identifier is required")
    private String identifier;
}
