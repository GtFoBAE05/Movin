package com.imannuel.movin.intrabanktransferservice.service.impl;

import com.imannuel.movin.intrabanktransferservice.client.BniFeignClient;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.account_inquiry.BniInternalAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.authorization.BniAuthorizationRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.balance.BniGetBalanceRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.transfer.BniTransferIntraBankAmountRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.transfer.BniTransferIntraBankRequest;
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
public class BniServiceImpl implements BankService {
    private final BniFeignClient bniFeignClient;
    private final RedisService redisService;

    @Value("${bank.bni.client-key}")
    private String clientKey;
    @Value("${bank.bni.client-secret}")
    private String clientSecret;
    @Value("${bank.bni.partner-id}")
    private String partnerId;
    @Value("${bank.bni.channel-id}")
    private String channelId;
    @Value("${bank.bni.source-account-number}")
    private String sourceAccountNumber;
    @Value("${bank.bni.private-key}")
    private String privateKey;

    @Override
    public AuthorizationResponse newAuthorizationToken() {
        log.info("Requesting BNI authorization token");

        String currentTimestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String stringToSign = clientKey + "|" + currentTimestamp;
        String signedString = SignatureUtility.signString(stringToSign, privateKey);

        BniAuthorizationRequest request = BniAuthorizationRequest.builder()
                .grantType("client_credentials")
                .build();

        AuthorizationResponse response = bniFeignClient.getAuthorizationToken(
                currentTimestamp,
                clientKey,
                signedString,
                request
        );
        log.info("Received BNI authorization token");

        redisService.save("access-token:bni", response.getAccessToken(), Duration.ofSeconds(response.getExpiresIn()));
        log.info("Saving BNI access token");

        return response;
    }

    @Override
    public String getAuthorizationToken() {
        Optional<String> accessToken = redisService.get("access-token:bni");
        return accessToken.orElseGet(() -> newAuthorizationToken().getAccessToken());
    }

    @Override
    public BalanceResponse getCurrentBalance() {
        log.info("Fetching BNI account balance");

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String externalId = IdentifierUtility.generateExternalId();
        String accessToken = getAuthorizationToken();

        BniGetBalanceRequest request = BniGetBalanceRequest.builder()
                .partnerReferenceNo("202010290000000000002")
                .accountNo(sourceAccountNumber)
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/snap-service/v1/balance-inquiry";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        BalanceResponse response = bniFeignClient.getBalance(
                timestamp,
                signature,
                partnerId,
                externalId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("BNI account balance fetched successfully");
        return response;
    }

    @Override
    public AccountInquiryResponse getAccountInquiry(String accountNumber) {
        log.info("Performing BNI account inquiry for accountNumber: {}", accountNumber);

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String externalId = IdentifierUtility.generateExternalId();
        String accessToken = getAuthorizationToken();

        BniInternalAccountInquiryRequest request = BniInternalAccountInquiryRequest.builder()
                .partnerReferenceNo("202010290000000000002")
                .beneficiaryAccountNo("317125693")
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/snap-service/v1/account-inquiry-internal";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        AccountInquiryResponse response = bniFeignClient.getInternalAccountInquiry(
                timestamp,
                signature,
                partnerId,
                externalId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("BNI account inquiry successful for accountNumber {} is {}", accountNumber, response.getAccountHolderName());
        return response;
    }

    @Override
    public IntraBankTransferResponse processIntrabankTransfer(String accountNumber, Long amount) {
        log.info("Initiating BNI intra-bank transfer: sourceAccount: {}, destinationAccount: {}, amount: {}",
                sourceAccountNumber, accountNumber, amount);

        String timestamp = DateTimeUtility.getCurrentIsoDateTimeUTC7();
        String externalId = IdentifierUtility.generateExternalId();
        String accessToken = getAuthorizationToken();

        BniTransferIntraBankAmountRequest bniTransferIntraBankAmountRequest = BniTransferIntraBankAmountRequest.builder()
                .value(CurrencyUtility.decimalAmountFormatter(55000))
                .currency("IDR")
                .build();

        BniTransferIntraBankRequest request = BniTransferIntraBankRequest.builder()
                .partnerReferenceNo("20220426170737356898")
                .amount(bniTransferIntraBankAmountRequest)
                .beneficiaryAccountNo("0115476151")
                .beneficiaryEmail("customer@outlook.com")
                .customerReference("20220426170737356898")
                .currency("IDR")
                .remark("20220426170737356898")
                .feeType("OUR")
                .sourceAccountNo(sourceAccountNumber)
                .transactionDate(timestamp)
                .build();
        String bodyHash = SignatureUtility.generateBodyHash(request);

        String httpMethod = "POST";
        String relativeUrl = "/snap-service/v1/transfer-intrabank";
        String signature = SignatureUtility.generateSignature(httpMethod, relativeUrl, accessToken, bodyHash, timestamp, clientSecret);

        IntraBankTransferResponse response = bniFeignClient.transferIntraBank(
                timestamp,
                signature,
                partnerId,
                externalId,
                "Bearer " + accessToken,
                channelId,
                request
        );

        log.info("BNI intra-bank transfer completed successfully for transaction with account number: {} and amount: {}", accountNumber, amount);
        return response;
    }
}
