package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.client.BcaFeignClient;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.account_inquiry.BcaInternalAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.authorization.BcaAuthorizationRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.balance.BcaGetBalanceRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.transfer.BcaTransferIntraBankAdditionalInfoRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.transfer.BcaTransferIntraBankAmountRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.transfer.BcaTransferIntraBankRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.AuthorizationResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.BalanceResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.IntraBankTransferResponse;
import com.imannuel.movin.intrabanktransferservice.service.BankService;
import com.imannuel.movin.intrabanktransferservice.service.RedisService;
import com.imannuel.movin.intrabanktransferservice.utility.CurrencyUtility;
import com.imannuel.movin.intrabanktransferservice.utility.DateTimeUtility;
import com.imannuel.movin.intrabanktransferservice.utility.IdentifierUtility;
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
public class BcaServiceImpl implements BankService {
    private final BcaFeignClient bcaFeignClient;
    private final RedisService redisService;

    @Value("${bank.bca.client-key}")
    private String clientKey;
    @Value("${bank.bca.client-secret}")
    private String clientSecret;
    @Value("${bank.bca.partner-id}")
    private String partnerId;
    @Value("${bank.bca.channel-id}")
    private String channelId;
    @Value("${bank.bca.source-account-number}")
    private String sourceAccountNumber;
    @Value("${bank.bca.private-key}")
    private String privateKey;

    @Override
    public AuthorizationResponse newAuthorizationToken() {
        log.info("Requesting BCA authorization token");

        String currentTimestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String stringToSign = clientKey + "|" + currentTimestamp;
        String signedString = SignatureUtility.signString(stringToSign, privateKey);

        BcaAuthorizationRequest request = BcaAuthorizationRequest.builder()
                .grantType("client_credentials")
                .build();

        AuthorizationResponse response = bcaFeignClient.getAuthorizationToken(
                currentTimestamp,
                clientKey,
                signedString,
                request
        );
        log.info("Received BCA authorization token");

        redisService.save("access-token:bca", response.getAccessToken(), Duration.ofSeconds(response.getExpiresIn()));
        log.info("Saving BCA access token");

        return response;
    }

    @Override
    public String getAuthorizationToken() {
        Optional<String> accessToken = redisService.get("access-token:bca");
        return accessToken.orElseGet(() -> newAuthorizationToken().getAccessToken());
    }

    @Override
    public BalanceResponse getCurrentBalance() {
        log.info("Fetching BCA account balance");

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String accessToken = getAuthorizationToken();

        BcaGetBalanceRequest request = BcaGetBalanceRequest.builder()
                .partnerReferenceNo("2020102900000000000001")
                .accountNo(sourceAccountNumber)
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/openapi/v1.0/balance-inquiry";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        BalanceResponse response = bcaFeignClient.getBalance(
                timestamp,
                signature,
                partnerId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("BCA account balance fetched successfully");
        return response;
    }

    @Override
    public AccountInquiryResponse getAccountInquiry(String accountNumber) {
        log.info("Performing BCA account inquiry for accountNumber: {}", accountNumber);

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String externalId = IdentifierUtility.generateExternalId();
        String accessToken = getAuthorizationToken();

        BcaInternalAccountInquiryRequest request = BcaInternalAccountInquiryRequest.builder()
                .partnerReferenceNo("202010290000000001")
                .beneficiaryAccountNo("8010001575")
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/openapi/v1.0/account-inquiry-internal";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        AccountInquiryResponse response = bcaFeignClient.getInternalAccountInquiry(
                timestamp,
                signature,
                partnerId,
                externalId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("BCA account inquiry successful for accountNumber {} is {}", accountNumber, response.getAccountHolderName());
        return response;
    }

    @Override
    public IntraBankTransferResponse processIntrabankTransfer(String accountNumber, Long amount) {
        log.info("Initiating BCA intra-bank transfer: sourceAccount: {}, destinationAccount: {}, amount: {}",
                sourceAccountNumber, accountNumber, amount);

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String accessToken = getAuthorizationToken();

        BcaTransferIntraBankAmountRequest bcaTransferIntraBankAmountRequest = BcaTransferIntraBankAmountRequest.builder()
                .value(CurrencyUtility.decimalAmountFormatter(Math.toIntExact(amount)))
                .currency("IDR")
                .build();

        BcaTransferIntraBankAdditionalInfoRequest bcaTransferIntraBankAdditionalInfoRequest = BcaTransferIntraBankAdditionalInfoRequest.builder()
                .economicActivity("Biaya Hidup Pihak Asing")
                .transactionPurpose("01")
                .build();

        BcaTransferIntraBankRequest request = BcaTransferIntraBankRequest.builder()
                .partnerReferenceNo("2020102900000000000001")
                .amount(bcaTransferIntraBankAmountRequest)
                .beneficiaryAccountNo("888801000157508")
                .beneficiaryEmail("yories.yolanda@work.co.id")
                .remark("remark test")
                .sourceAccountNo("888801000157508")
                .transactionDate(timestamp)
                .additionalInfo(bcaTransferIntraBankAdditionalInfoRequest)
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/openapi/v1.0/transfer-intrabank";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        IntraBankTransferResponse response = bcaFeignClient.transferIntraBank(
                timestamp,
                signature,
                partnerId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("BCA intra-bank transfer completed successfully for transaction with account number: {} and amount: {}", accountNumber, amount);
        return response;
    }

}
