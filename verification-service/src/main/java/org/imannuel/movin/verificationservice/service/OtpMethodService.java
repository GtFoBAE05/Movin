package org.imannuel.movin.verificationservice.service;

import org.imannuel.movin.verificationservice.enums.OtpMethod;

public interface OtpMethodService {
    OtpMethod findByName(String name);
}
