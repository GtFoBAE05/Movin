package org.imannuel.movin.verificationservice.service;

import org.imannuel.movin.verificationservice.enums.VerificationStatus;

public interface VerificationStatusService {
    VerificationStatus findByName(String name);
}
