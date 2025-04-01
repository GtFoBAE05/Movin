package org.imannuel.movin.userservice.service.impl;

import com.imannuel.movin.commonservice.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.userservice.dto.mapper.CustomerMapper;
import org.imannuel.movin.userservice.dto.request.CustomerRegisterRequest;
import org.imannuel.movin.userservice.dto.response.CustomerRegisterResponse;
import org.imannuel.movin.userservice.entity.Customer;
import org.imannuel.movin.userservice.enums.Role;
import org.imannuel.movin.userservice.repository.CustomerRepository;
import org.imannuel.movin.userservice.service.CustomerService;
import org.imannuel.movin.userservice.service.RoleService;
import org.imannuel.movin.userservice.utility.ReferralCodeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final RoleService roleService;
    private final ValidationUtility validationUtility;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Customer createCustomer(Customer customer) {
        log.debug("Creating new customer with email: {}", customer.getEmail());
        Customer savedCustomer = customerRepository.saveAndFlush(customer);
        log.info("Successfully created customer with ID: {}", savedCustomer.getId());
        return savedCustomer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerRegisterResponse registerCustomer(CustomerRegisterRequest registerRequest) {
        log.debug("Processing customer registration request for email: {}", registerRequest.getEmail());

        validationUtility.validate(registerRequest);
        log.debug("Validation passed for customer registration request");

        Customer customer = createCustomerFromRegisterRequest(registerRequest);
        customerRepository.saveAndFlush(customer);
        log.info("Successfully registered new customer with ID: {} and email: {}", customer.getId(), customer.getEmail());

        return CustomerMapper.toCustomerRegisterResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findCustomerById(String userId) {
        log.debug("Finding customer by ID: {}", userId);
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Customer not found with ID: {}", userId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
                });

        log.info("Successfully found customer with ID: {}", userId);
        return customer;
    }

    @Override
    @Transactional(readOnly = true)
    public String generateReferralCode() {
        log.debug("Generating unique referral code");

        String referralCode;
        int attempts = 0;
        final int MAX_ATTEMPTS = 10;

        do {
            referralCode = ReferralCodeGenerator.generateReferralCode();
            attempts++;

            if (attempts > MAX_ATTEMPTS) {
                log.warn("Exceeded maximum attempts to generate unique referral code.");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to generate unique referral code");
            }
        } while (checkReferralCodeAvailability(referralCode));

        log.info("Successfully generated unique referral code: {} after {} attempts", referralCode, attempts);
        return referralCode;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkReferralCodeAvailability(String referralCode) {
        log.debug("Checking availability of referral code: {}", referralCode);
        boolean isAvailable = customerRepository.existsByReferralCode(referralCode);
        log.info("Referral code {} available: {}", referralCode, isAvailable);
        return isAvailable;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOtpVerifiedStatus(String userId, String type) {
        log.debug("Updating OTP verified status for customer ID: {}", userId);

        Customer customer = findCustomerById(userId);
        switch (type) {
            case "EMAIL":
                customer.setEmailVerified(true);
                log.info("Successfully updated email verification status for customer ID: {}", userId);
                break;
            case "SMS", "WHATSAPP":
                customer.setPhoneVerified(true);
                log.info("Successfully updated phone verification status for customer ID: {}", userId);
                break;
            case "IDENTITY":
                customer.setIdentityVerified(true);
                log.info("Successfully updated identity verification status for customer ID: {}", userId);
                break;
            default:
                log.warn("Unknown verification type: {}", type);
                return;
        }

        customerRepository.save(customer);
        log.info("Successfully saved updated customer verification status for customer ID: {}", userId);
    }


    @Transactional
    protected Customer createCustomerFromRegisterRequest(CustomerRegisterRequest request) {
        log.debug("Creating customer entity from registration request");

        Role role = roleService.findRole(Role.ERole.ROLE_CUSTOMER.name());
        String referralCode = generateReferralCode();

        return Customer.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .balance(0)
                .role(role)
                .referralCode(referralCode)
                .build();
    }
}