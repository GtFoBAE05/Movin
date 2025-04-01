package com.imannuel.movin.intrabanktransferservice.controller;

import com.imannuel.movin.commonservice.dto.mapper.ResponseMapper;
import com.imannuel.movin.intrabanktransferservice.dto.request.BankAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.CreateIntraBankTransactionRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.BankAccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.IntraBankTransactionResponse;
import com.imannuel.movin.intrabanktransferservice.service.BankAccountService;
import com.imannuel.movin.intrabanktransferservice.service.IntraBankTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/intra-bank")
@RequiredArgsConstructor
public class IntraBankTransferController {
    private final BankAccountService bankAccountService;
    private final IntraBankTransactionService intraBankTransactionService;

    @PostMapping("/account-inquiry")
    public ResponseEntity<?> accountInquiry(
            @RequestBody BankAccountInquiryRequest bankAccountInquiryRequest
    ) {
        BankAccountInquiryResponse bankAccountInquiryResponse = bankAccountService.getBankAccountInquiry(bankAccountInquiryRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK, "Success get account inquiry",
                bankAccountInquiryResponse);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> intraBankTransfer(
            @RequestHeader("X-USER-ID") String userid,
            @RequestBody CreateIntraBankTransactionRequest createIntraBankTransactionRequest
    ) {
        IntraBankTransactionResponse intraBankTransactionResponse = intraBankTransactionService.createIntraBankTransaction(userid, createIntraBankTransactionRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.CREATED, "Success create intra bank transaction",
                intraBankTransactionResponse);
    }

    @PostMapping("/transfer/{id}/receive-payment")
    public ResponseEntity<?> receiveIntraBankTransferPayment(
            @PathVariable("id") String intraBankTransactionId
    ) {
        IntraBankTransactionResponse intraBankTransactionResponse = intraBankTransactionService.receiveIntraBankTransactionPayment(intraBankTransactionId);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK, "Success receive intra bank transaction payment",
                intraBankTransactionResponse);
    }
}
