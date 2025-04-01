package org.imannuel.movin.userservice.service;

import org.imannuel.movin.userservice.dto.response.CustomerRegistrationValidate;
import org.imannuel.movin.userservice.entity.User;

public interface UserService {
    User findById(String id);

    User findByPhoneNumberOrEmail(String phoneNumberOrEmail);

    CustomerRegistrationValidate validateUserAvailability(String identifier, boolean isPhone);
}
