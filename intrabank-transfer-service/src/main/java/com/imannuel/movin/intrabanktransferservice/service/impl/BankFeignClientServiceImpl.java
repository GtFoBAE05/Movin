package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.client.BankFeignClient;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.internal.BankFeignResponse;
import com.imannuel.movin.intrabanktransferservice.service.BankFeignClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankFeignClientServiceImpl implements BankFeignClientService {
    private final BankFeignClient bankFeignClient;

    @Override
    public BankFeignResponse findBankByCode(String code) {
        log.debug("Finding bank by code: {}", code);
        try {
            BankFeignResponse bank = bankFeignClient.findBankByCode(code);
            log.debug("Bank found with code: {}", code);
            return bank;
        } catch (Exception e) {
            log.error("Error finding bank with code: {}", code, e);
            throw e;
        }
    }

    @Override
    public BankFeignResponse findBankById(String id) {
        log.debug("Finding bank by id: {}", id);
        try {
            BankFeignResponse bank = bankFeignClient.findBankById(id);
            log.debug("Bank found with id: {}", id);
            return bank;
        } catch (Exception e) {
            log.error("Error finding bank with id: {}", id, e);
            throw e;
        }
    }
}
