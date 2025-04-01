package com.imannuel.movin.authenticationservice.client;

import com.imannuel.movin.authenticationservice.dto.request.CustomerRegisterRequest;
import com.imannuel.movin.authenticationservice.dto.response.CustomerRegisterResponse;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.CustomerRegistrationValidateFeignResponse;
import com.imannuel.movin.authenticationservice.dto.response.feign.internal.UserFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping("/api/users/phone-number/availability")
    CustomerRegistrationValidateFeignResponse registerPhoneNumberAvailability(
            @RequestParam(name = "phone") String phone
    );

    @GetMapping("/api/users/email/availability")
    CustomerRegistrationValidateFeignResponse registerEmailAvailability(
            @RequestParam(name = "email") String email
    );

    @GetMapping("/api/users/find-by-id")
    UserFeignResponse findUserById(
            @RequestParam(name = "user-id") String userId
    );

    @GetMapping("/api/users/find-by-identifier")
    UserFeignResponse findUserByIdentifier(
            @RequestParam(name = "identifier") String identifier
    );

    @PostMapping("/api/customers/register")
    CustomerRegisterResponse registerCustomer(
            @RequestBody CustomerRegisterRequest customerRegisterRequest
    );

}
