package org.imannuel.movin.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.userservice.dto.response.CustomerRegistrationValidate;
import org.imannuel.movin.userservice.entity.User;
import org.imannuel.movin.userservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/phone-number/availability")
    public CustomerRegistrationValidate registerPhoneNumberAvailability(
            @RequestParam(name = "phone") String phone
    ) {
        log.info("Received request to check phone number availability: {}", phone);
        CustomerRegistrationValidate result = userService.validateUserAvailability(phone, true);
        log.info("Successfully checked phone number availability: {} -> {}", phone, result.isPhoneVerification());
        return result;
    }

    @GetMapping("/email/availability")
    public CustomerRegistrationValidate registerEmailAvailability(
            @RequestParam(name = "email") String email
    ) {
        log.info("Received request to check email availability: {}", email);
        CustomerRegistrationValidate result = userService.validateUserAvailability(email, false);
        log.info("Successfully checked email availability: {} -> {}", email, result.isEmailVerification());
        return result;
    }

    @GetMapping("/find-by-id")
    public User findUserById(
            @RequestParam(name = "user-id") String userId
    ) {
        log.info("Received request to fetch user with ID: {}", userId);
        User user = userService.findById(userId);
        log.info("Successfully retrieved user with ID: {}", userId);
        return user;
    }

    @GetMapping("/find-by-identifier")
    public User findUserByIdentifier(
            @RequestParam(name = "identifier") String identifier
    ) {
        log.info("Received request to fetch user with identifier: {}", identifier);
        User user = userService.findByPhoneNumberOrEmail(identifier);
        log.info("Successfully retrieved user with identifier: {}", identifier);
        return user;
    }
}