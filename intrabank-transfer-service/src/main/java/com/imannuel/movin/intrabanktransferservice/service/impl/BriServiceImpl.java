package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.client.BriFeignClient;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.account_inquiry.BriInternalAccountInquiryAdditionalInfoRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.account_inquiry.BriInternalAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.authorization.BriAuthorizationRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.balance.BriGetBalanceRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.transfer.BriTransferIntraBankAdditionalInfoRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.transfer.BriTransferIntraBankAmountRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.transfer.BriTransferIntraBankRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AuthorizationResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.BalanceResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.IntraBankTransferResponse;
import com.imannuel.movin.intrabanktransferservice.service.BankService;
import com.imannuel.movin.intrabanktransferservice.service.RedisService;
import com.imannuel.movin.intrabanktransferservice.utility.CurrencyUtility;
import com.imannuel.movin.intrabanktransferservice.utility.DateTimeUtility;
import com.imannuel.movin.intrabanktransferservice.utility.SignatureUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BriServiceImpl implements BankService {
    private final BriFeignClient briFeignClient;
    private final RedisService redisService;

    @Value("${bank.bri.client-key}")
    private String clientKey;
    @Value("${bank.bri.client-secret}")
    private String clientSecret;
    @Value("${bank.bri.partner-id}")
    private String partnerId;
    @Value("${bank.bri.channel-id}")
    private String channelId;
    @Value("${bank.bri.source-account-number}")
    private String sourceAccountNumber;
    @Value("${bank.bri.private-key}")
    private String privateKey;


    @Override
    public AuthorizationResponse newAuthorizationToken() {
        log.info("Requesting BRI authorization token");

        String currentTimestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String stringToSign = clientKey + "|" + currentTimestamp;
        String signedString = SignatureUtility.signString(stringToSign, privateKey);

        BriAuthorizationRequest request = BriAuthorizationRequest.builder()
                .grantType("client_credentials")
                .build();

        AuthorizationResponse response = briFeignClient.getAuthorizationToken(
                currentTimestamp,
                clientKey,
                signedString,
                request
        );
        log.info("Received BRI authorization token");

        redisService.save("access-token:bri", response.getAccessToken(), Duration.ofSeconds(response.getExpiresIn()));
        log.info("Saving BRI access token");

        return response;
    }

    @Override
    public String getAuthorizationToken() {
        Optional<String> accessToken = redisService.get("access-token:bri");
        return accessToken.orElseGet(() -> newAuthorizationToken().getAccessToken());
    }

    @Override
    public BalanceResponse getCurrentBalance() {
        log.info("Fetching BRI account balance");

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String externalId = "123";
        String accessToken = getAuthorizationToken();

        BriGetBalanceRequest request = BriGetBalanceRequest.builder()
                .accountNo(sourceAccountNumber)
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/snap/v1.0/balance-inquiry";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        BalanceResponse response = briFeignClient.getBalance(
                timestamp,
                signature,
                partnerId,
                externalId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("BRI account balance fetched successfully");
        return response;
    }

    @Override
    public AccountInquiryResponse getAccountInquiry(String accountNumber) {
        log.info("Performing BRI account inquiry for accountNumber: {}", accountNumber);

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String externalId = "123";
        String accessToken = getAuthorizationToken();

        BriInternalAccountInquiryAdditionalInfoRequest accountInquiryAdditionalInfoRequest = BriInternalAccountInquiryAdditionalInfoRequest.builder()
                .deviceId("12345679237")
                .channel("mobilephone")
                .build();

        BriInternalAccountInquiryRequest request = BriInternalAccountInquiryRequest.builder()
                .beneficiaryAccountNo("888801000157508")
                .additionalInfo(accountInquiryAdditionalInfoRequest)
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/intrabank/snap/v2.0/account-inquiry-internal";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        AccountInquiryResponse response = briFeignClient.getInternalAccountInquiry(
                timestamp,
                signature,
                partnerId,
                externalId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("BRI account inquiry successful for accountNumber {} is {}", accountNumber, response.getAccountHolderName());
        return response;
    }

    @Override
    public IntraBankTransferResponse processIntrabankTransfer(String accountNumber, Long amount) {
        log.info("Initiating BRI intra-bank transfer: sourceAccount: {}, destinationAccount: {}, amount: {}",
                "888801000157610", accountNumber, amount);

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String externalId = "103539";
        String accessToken = getAuthorizationToken();

        BriTransferIntraBankAmountRequest briTransferIntraBankAmountRequest = BriTransferIntraBankAmountRequest.builder()
                .value(CurrencyUtility.decimalAmountFormatter(Math.toIntExact(amount)))
                .currency("IDR")
                .build();

        BriTransferIntraBankAdditionalInfoRequest transferIntraBankAdditionalInfoRequest = BriTransferIntraBankAdditionalInfoRequest.builder()
                .deviceId("12345679237")
                .channel("mobilephone")
                .build();

        BriTransferIntraBankRequest request = BriTransferIntraBankRequest.builder()
                .partnerReferenceNo("3c08d3739ff5409b9c4f9fb6")
                .amount(briTransferIntraBankAmountRequest)
                .beneficiaryAccountNo("888801000157508")
                .customerReference("10052031")
                .remark("remark test")
                .feeType("BEN")
                .sourceAccountNo(sourceAccountNumber)
                .transactionDate(timestamp)
                .additionalInfo(transferIntraBankAdditionalInfoRequest)
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/intrabank/snap/v2.0/transfer-intrabank";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        IntraBankTransferResponse response = briFeignClient.transferIntraBank(
                timestamp,
                signature,
                partnerId,
                externalId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("BRI intra-bank transfer completed successfully for transaction with account number: {} and amount: {}", accountNumber, amount);
        return response;
    }
}

