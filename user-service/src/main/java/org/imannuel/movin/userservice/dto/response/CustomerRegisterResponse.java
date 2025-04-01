package org.imannuel.movin.userservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRegisterResponse {
    private String id;

    private String phoneNumber;

    private String email;

    private String fullName;
}
