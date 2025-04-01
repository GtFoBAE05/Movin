package org.imannuel.movin.userservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRegistrationValidate {
    private boolean registered;
    
    private boolean phoneVerification;

    private boolean emailVerification;

    private boolean pinSetup;
}
