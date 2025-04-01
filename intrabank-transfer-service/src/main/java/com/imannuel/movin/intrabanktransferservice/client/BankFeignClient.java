package com.imannuel.movin.intrabanktransferservice.client;

import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.BankFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("bank-service")
public interface BankFeignClient {
    @GetMapping("/api/banks/{code}")
    BankFeignResponse findBankByCode(
            @PathVariable(name = "code") String code
    );

    @GetMapping("/api/banks/id/{id}")
    BankFeignResponse findBankById(
            @PathVariable(name = "id") String id
    );
}
