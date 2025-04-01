package org.imannuel.movin.userservice.dto.mapper;

import org.imannuel.movin.userservice.dto.response.CustomerRegisterResponse;
import org.imannuel.movin.userservice.entity.Customer;

public class CustomerMapper {
    public static CustomerRegisterResponse toCustomerRegisterResponse(Customer customer) {
        return CustomerRegisterResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }
}
