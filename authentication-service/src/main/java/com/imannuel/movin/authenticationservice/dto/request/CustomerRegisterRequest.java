package com.imannuel.movin.authenticationservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRegisterRequest {
    @NotBlank(message = "phoneNumber is required")
    @Size(min = 10, max = 15, message = "phone number should have length between 10 - 15 digit")
    private String phoneNumber;

    @NotBlank(message = "fullName is required")
    private String fullName;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;
}
