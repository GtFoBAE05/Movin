package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.client.MandiriFeignClient;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.account_inquiry.MandiriInternalAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.authorization.MandiriAuthorizationRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.balance.MandiriGetBalanceRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.transfer.MandiriTransferIntraBankAdditionalInfoRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.transfer.MandiriTransferIntraBankAmountRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.transfer.MandiriTransferIntraBankRequest;
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
public class MandiriServiceImpl implements BankService {
    private final MandiriFeignClient mandiriFeignClient;
    private final RedisService redisService;

    @Value("${bank.mandiri.client-key}")
    private String clientKey;
    @Value("${bank.mandiri.client-secret}")
    private String clientSecret;
    @Value("${bank.mandiri.partner-id}")
    private String partnerId;
    @Value("${bank.mandiri.channel-id}")
    private String channelId;
    @Value("${bank.mandiri.source-account-number}")
    private String sourceAccountnumber;
    @Value("${bank.mandiri.private-key}")
    private String privateKey;

    @Override
    public AuthorizationResponse newAuthorizationToken() {
        log.info("Requesting Mandiri authorization token");

        String currentTimestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String stringToSign = clientKey + "|" + currentTimestamp;
        String signedString = SignatureUtility.signString(stringToSign, privateKey);

        MandiriAuthorizationRequest request = MandiriAuthorizationRequest.builder()
                .grantType("client_credentials")
                .build();

        AuthorizationResponse response = mandiriFeignClient.getAuthorizationToken(
                currentTimestamp,
                clientKey,
                signedString,
                request
        );
        log.info("Received Mandiri authorization token");

        redisService.save("access-token:mandiri", response.getAccessToken(), Duration.ofSeconds(response.getExpiresIn()));
        log.info("Saving Mandiri access token");

        return response;
    }

    @Override
    public String getAuthorizationToken() {
        Optional<String> accessToken = redisService.get("access-token:mandiri");
        return accessToken.orElseGet(() -> newAuthorizationToken().getAccessToken());
    }

    @Override
    public BalanceResponse getCurrentBalance() {
        log.info("Fetching Mandiri account balance");

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String externalId = "200001";
        String accessToken = getAuthorizationToken();

        MandiriGetBalanceRequest request = MandiriGetBalanceRequest.builder()
                .accountNo(sourceAccountnumber)
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/openapi/customers/v2.1/balance-inquiry";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        BalanceResponse response = mandiriFeignClient.getBalance(
                timestamp,
                signature,
                partnerId,
                externalId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("Mandiri account balance fetched successfully");
        return response;
    }

    @Override
    public AccountInquiryResponse getAccountInquiry(String accountNumber) {
        log.info("Performing Mandiri account inquiry for accountNumber: {}", accountNumber);

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String externalId = "200001";
        String accessToken = getAuthorizationToken();

        MandiriInternalAccountInquiryRequest request = MandiriInternalAccountInquiryRequest.builder()
                .accountNo(accountNumber)
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/openapi/customers/v2.0/account-inquiry-internal";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        AccountInquiryResponse response = mandiriFeignClient.getInternalAccountInquiry(
                timestamp,
                signature,
                partnerId,
                externalId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("Mandiri account inquiry successful for accountNumber {} is {}", accountNumber, response.getAccountHolderName());
        return response;
    }

    @Override
    public IntraBankTransferResponse processIntrabankTransfer(String accountNumber, Long amount) {
        log.info("Initiating Mandiri intra-bank transfer: sourceAccount: {}, destinationAccount: {}, amount: {}",
                sourceAccountnumber, accountNumber, amount);

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String externalId = "200005";
        String accessToken = getAuthorizationToken();

        MandiriTransferIntraBankAmountRequest mandiriTransferIntraBankAmountRequest = MandiriTransferIntraBankAmountRequest.builder()
                .value(CurrencyUtility.decimalAmountFormatter(Math.toIntExact(amount)))
                .currency("IDR")
                .build();

        MandiriTransferIntraBankAdditionalInfoRequest intraBankAdditionalInfoRequest = MandiriTransferIntraBankAdditionalInfoRequest.builder()
                .reportCode("abcd")
                .senderInstrument("1")
                .senderAccountNo(accountNumber)
                .senderName("rendimatrido")
                .senderCountry("id")
                .senderCustomerType("1")
                .beneficiaryAccountName("AjojiAjojo")
                .beneficiaryInstrument("1")
                .beneficiaryCustomerType("1")
                .build();

        MandiriTransferIntraBankRequest request = MandiriTransferIntraBankRequest.builder()
                .partnerReferenceNo("ITN0001")
                .amount(mandiriTransferIntraBankAmountRequest)
                .beneficiaryAccountNo(accountNumber)
                .beneficiaryEmail("sandbox_test@gmail.com")
                .remark("PTROpenAPI")
                .sourceAccountNo(sourceAccountnumber)
                .transactionDate(timestamp)
                .additionalInfo(intraBankAdditionalInfoRequest)
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/openapi/transactions/v2.0/transfer-intrabank";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        IntraBankTransferResponse response = mandiriFeignClient.transferIntraBank(
                timestamp,
                signature,
                partnerId,
                externalId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("Mandiri intra-bank transfer completed successfully for transaction with account number: {} and amount: {}", accountNumber, amount);
        return response;
    }
}