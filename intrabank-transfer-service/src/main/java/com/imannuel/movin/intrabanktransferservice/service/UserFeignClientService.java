package com.imannuel.movin.intrabanktransferservice.service;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.UserFeignResponse;

public interface UserFeignClientService {
    UserFeignResponse findUserByUserId(String userId);
}
