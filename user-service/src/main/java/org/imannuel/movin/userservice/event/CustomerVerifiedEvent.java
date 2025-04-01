package org.imannuel.movin.userservice.event;

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
