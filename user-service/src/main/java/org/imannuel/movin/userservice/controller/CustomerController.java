package org.imannuel.movin.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.userservice.dto.request.CustomerRegisterRequest;
import org.imannuel.movin.userservice.dto.response.CustomerRegisterResponse;
import org.imannuel.movin.userservice.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register")
    public CustomerRegisterResponse registerCustomer(
            @RequestBody CustomerRegisterRequest customerRegisterRequest
    ) {
        log.info("Received customer registration request for email: {}", customerRegisterRequest.getEmail());
        CustomerRegisterResponse response = customerService.registerCustomer(customerRegisterRequest);
        log.info("Successfully registered customer with ID: {}", response.getId());
        return response;
    }
}
