package com.imannuel.movin.authenticationservice.dto.response.feign.internal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRegistrationValidateFeignResponse {
    private boolean registered;
    
    private boolean phoneVerification;

    private boolean emailVerification;

    private boolean pinSetup;
}
