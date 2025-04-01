package org.imannuel.movin.userservice.service;

import org.imannuel.movin.userservice.dto.request.CustomerRegisterRequest;
import org.imannuel.movin.userservice.dto.response.CustomerRegisterResponse;
import org.imannuel.movin.userservice.entity.Customer;

public interface CustomerService {
    Customer createCustomer(Customer customer);

    CustomerRegisterResponse registerCustomer(CustomerRegisterRequest registerRequest);

    Customer findCustomerById(String userId);

    String generateReferralCode();

    boolean checkReferralCodeAvailability(String referralCode);

    void updateOtpVerifiedStatus(String userId, String type);
}
