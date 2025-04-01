package com.imannuel.movin.intrabanktransferservice.client;

import com.imannuel.movin.intrabanktransferservice.configuration.FeignConfiguration;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.account_inquiry.BcaInternalAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.authorization.BcaAuthorizationRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.balance.BcaGetBalanceRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bca.transfer.BcaTransferIntraBankRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.account_inquiry.BcaInternalAccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.authorization.BcaAuthorizationResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.balance.BcaGetBalanceResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bca.transfer.BcaTransferIntraBankResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "bcaFeignClient", url = "${bank.bca.sandbox-url}", configuration = FeignConfiguration.class)
public interface BcaFeignClient {
    @PostMapping(
            path = "/openapi/v1.0/access-token/b2b"
    )
    BcaAuthorizationResponse getAuthorizationToken(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-CLIENT-KEY") String clientKey,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestBody BcaAuthorizationRequest request
    );

    @PostMapping(
            path = "/openapi/v1.0/balance-inquiry"
    )
    BcaGetBalanceResponse getBalance(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody BcaGetBalanceRequest request
    );

    @PostMapping(
            path = "/openapi/v1.0/account-inquiry-internal"
    )
    BcaInternalAccountInquiryResponse getInternalAccountInquiry(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "X-EXTERNAL-ID") String externalId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody BcaInternalAccountInquiryRequest request
    );

    @PostMapping(
            path = "/openapi/v1.0/transfer-intrabank"
    )
    BcaTransferIntraBankResponse transferIntraBank(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody BcaTransferIntraBankRequest request
    );
}
