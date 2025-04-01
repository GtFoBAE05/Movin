package com.imannuel.movin.intrabanktransferservice.client;

import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.account_inquiry.BniInternalAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.authorization.BniAuthorizationRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.balance.BniGetBalanceRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bni.transfer.BniTransferIntraBankRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bni.account_inquiry.BniInternalAccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bni.authorization.BniAuthorizationResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bni.balance.BniGetBalanceResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bni.transfer.BniTransferIntraBankResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "BniFeignClient", url = "${bank.bni.sandbox-url}")
public interface BniFeignClient {
    @PostMapping(
            value = "/snap/v1/access-token/b2b"
    )
    @ResponseBody
    BniAuthorizationResponse getAuthorizationToken(
            @RequestHeader("X-TIMESTAMP") String timestamp,
            @RequestHeader("X-CLIENT-KEY") String clientKey,
            @RequestHeader("X-SIGNATURE") String signature,
            @RequestBody BniAuthorizationRequest request
    );

    @PostMapping(
            value = "/snap-service/v1/balance-inquiry"
    )
    BniGetBalanceResponse getBalance(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "X-EXTERNAL-ID") String externalId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody BniGetBalanceRequest request
    );

    @PostMapping(
            value = "/snap-service/v1/account-inquiry-internal"
    )
    BniInternalAccountInquiryResponse getInternalAccountInquiry(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "X-EXTERNAL-ID") String externalId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody BniInternalAccountInquiryRequest request
    );

    @PostMapping(
            value = "/snap-service/v1/transfer-intrabank"
    )
    BniTransferIntraBankResponse transferIntraBank(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "X-EXTERNAL-ID") String externalId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody BniTransferIntraBankRequest request
    );
}
