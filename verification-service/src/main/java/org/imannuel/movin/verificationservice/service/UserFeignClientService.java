package org.imannuel.movin.verificationservice.service;

import org.imannuel.movin.verificationservice.dto.response.feign.internal.UserFeignResponse;

public interface UserFeignClientService {
    UserFeignResponse findUserByIdentifier(String identifier);

    UserFeignResponse findUserByUserId(String userId);
}
