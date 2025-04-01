package org.imannuel.movin.verificationservice.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerVerifiedEvent {
    private String userId;

    private String VerificationType;
}
