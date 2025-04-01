package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BankServiceFactory {
    private final Map<String, BankService> bankServices;

    @Autowired
    public BankServiceFactory(List<BankService> bankServiceList) {
        this.bankServices = new HashMap<>();

        for (BankService bankService : bankServiceList) {
            if (bankService instanceof BcaServiceImpl) {
                bankServices.put("bca", bankService);
            } else if (bankService instanceof BniServiceImpl) {
                bankServices.put("bni", bankService);
            } else if (bankService instanceof MandiriServiceImpl) {
                bankServices.put("mandiri", bankService);
            } else if (bankService instanceof BriServiceImpl) {
                bankServices.put("bri", bankService);
            }
        }

        log.info("BankServiceFactory initialized with supported banks: {}", bankServices.keySet());
    }

    public BankService getBankService(String bankCode) {
        log.info("Fetching bank service for bankCode: {}", bankCode);

        BankService bankService = bankServices.get(bankCode);
        if (bankService == null) {
            log.error("Unsupported bank requested: bankCode: {}", bankCode);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Bank not supported: " + bankCode);
        }

        log.info("Bank service found for bankCode: {}", bankCode);
        return bankService;
    }
}