package org.imannuel.movin.bankservice.controller;

import com.imannuel.movin.commonservice.dto.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.bankservice.dto.request.BankRequest;
import org.imannuel.movin.bankservice.dto.request.PaymentOptionsRequest;
import org.imannuel.movin.bankservice.dto.response.BankCustomerResponse;
import org.imannuel.movin.bankservice.dto.response.BankResponse;
import org.imannuel.movin.bankservice.entity.Bank;
import org.imannuel.movin.bankservice.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/banks")
@RequiredArgsConstructor
public class BankController {
    private final BankService bankService;

    @GetMapping()
    public ResponseEntity<?> getBanksForCustomer() {
        log.info("Received request to fetch banks for customer");
        List<BankCustomerResponse> bankCustomerResponseList = bankService.getAllBanksForCustomer();
        log.info("Successfully retrieved {} banks for customer", bankCustomerResponseList.size());
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK, "Success fetch banks for customer",
                bankCustomerResponseList);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getBanksForAdmin() {
        log.info("Received request to fetch banks for admin");
        List<BankResponse> bankResponseList = bankService.getAllBanksForAdmin();
        log.info("Successfully retrieved {} banks for admin", bankResponseList.size());
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK, "Success fetch banks for admin",
                bankResponseList);
    }

    @PostMapping
    public ResponseEntity<?> createBank(
            @RequestBody BankRequest bankRequest
    ) {
        log.info("Received request to create bank: {}", bankRequest);
        BankResponse bank = bankService.createBank(bankRequest);
        log.info("Successfully created bank with code: {}", bank.getCode());
        return ResponseMapper.toCommonResponse(true, HttpStatus.CREATED, "Success create bank", bank);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateBank(
            @PathVariable(name = "code") String code,
            @RequestBody BankRequest bankRequest
    ) {
        log.info("Received request to update bank with code: {}", code);
        BankResponse bank = bankService.updateBank(code, bankRequest);
        log.info("Successfully updated bank with code: {}", code);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK, "Success update bank", bank);
    }

    @GetMapping("/payment-options")
    public ResponseEntity<?> getAvailablePaymentOptions(
            @RequestBody PaymentOptionsRequest paymentOptionsRequest
    ) {
        log.info("Received request to get available payment options: {}", paymentOptionsRequest);
        List<BankCustomerResponse> availablePaymentOptions =
                bankService.getAvailablePaymentOptions(paymentOptionsRequest);
        log.info("Successfully retrieved {} payment options", availablePaymentOptions.size());
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                "Success get available payment options", availablePaymentOptions);
    }

    @GetMapping("/{code}")
    public Bank getBank(
            @PathVariable(name = "code") String code
    ) {
        log.info("Received request to fetch bank with code: {}", code);
        Bank bank = bankService.findBank(code);
        log.info("Successfully retrieved bank with code: {}", code);
        return bank;
    }

    @GetMapping("/id/{id}")
    public Bank getBankById(
            @PathVariable(name = "id") String id
    ) {
        log.info("Received request to fetch bank with ID: {}", id);
        Bank bank = bankService.findBankById(id);
        log.info("Successfully retrieved bank with ID: {}", id);
        return bank;
    }
}