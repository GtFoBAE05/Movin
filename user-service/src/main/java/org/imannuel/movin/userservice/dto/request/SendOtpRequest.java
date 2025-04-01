package org.imannuel.movin.userservice.dto.request;

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

    @NotBlank(message = "userId is required")
    private String userId;
}
