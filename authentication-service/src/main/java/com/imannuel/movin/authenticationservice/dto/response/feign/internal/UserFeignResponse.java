package com.imannuel.movin.authenticationservice.dto.response.feign.internal;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFeignResponse {
    private String id;

    private String phoneNumber;

    private String fullName;

    private String email;

    private String referralCode;

    private RoleFeignResponse role;
}
