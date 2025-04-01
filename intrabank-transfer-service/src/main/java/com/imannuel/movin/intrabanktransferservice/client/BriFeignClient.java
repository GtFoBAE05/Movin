package com.imannuel.movin.intrabanktransferservice.client;

import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.account_inquiry.BriInternalAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.authorization.BriAuthorizationRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.balance.BriGetBalanceRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.bri.transfer.BriTransferIntraBankRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.account_inquiry.BriInternalAccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.authorization.BriAuthorizationResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.balance.BriGetBalanceResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.bri.transfer.BriTransferIntraBankResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "BriFeignClient", url = "${bank.bri.sandbox-url}")
public interface BriFeignClient {
    @PostMapping(
            value = "/snap/v1.0/access-token/b2b"
    )
    @ResponseBody
    BriAuthorizationResponse getAuthorizationToken(
            @RequestHeader("X-TIMESTAMP") String timestamp,
            @RequestHeader("X-CLIENT-KEY") String clientKey,
            @RequestHeader("X-SIGNATURE") String signature,
            @RequestBody BriAuthorizationRequest request
    );

    @PostMapping(
            value = "/snap/v1.0/balance-inquiry"
    )
    BriGetBalanceResponse getBalance(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "X-EXTERNAL-ID") String externalId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody BriGetBalanceRequest request
    );

    @PostMapping(
            value = "/intrabank/snap/v2.0/account-inquiry-internal"
    )
    BriInternalAccountInquiryResponse getInternalAccountInquiry(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "X-EXTERNAL-ID") String externalId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody BriInternalAccountInquiryRequest request
    );

    @PostMapping(
            value = "/intrabank/snap/v2.0/transfer-intrabank"
    )
    BriTransferIntraBankResponse transferIntraBank(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "X-EXTERNAL-ID") String externalId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody BriTransferIntraBankRequest request
    );
}
