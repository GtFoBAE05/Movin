package com.imannuel.movin.intrabanktransferservice.client;


import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.UserFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserFeignClient {
    @GetMapping("/api/users/find-by-id")
    UserFeignResponse findUserById(
            @RequestParam(name = "user-id") String userId
    );
}
