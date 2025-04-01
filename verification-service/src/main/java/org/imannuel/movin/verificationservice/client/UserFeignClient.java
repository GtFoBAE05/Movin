package org.imannuel.movin.verificationservice.client;

import org.imannuel.movin.verificationservice.dto.response.feign.internal.UserFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping("/api/users/find-by-id")
    UserFeignResponse findUserById(
            @RequestParam(name = "user-id") String userId
    );

    @GetMapping("/api/users/find-by-identifier")
    UserFeignResponse findUserByIdentifier(
            @RequestParam(name = "identifier") String identifier
    );
}
