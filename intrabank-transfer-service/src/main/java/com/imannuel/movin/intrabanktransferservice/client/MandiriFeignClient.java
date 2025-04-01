package com.imannuel.movin.intrabanktransferservice.client;

import com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.account_inquiry.MandiriInternalAccountInquiryRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.authorization.MandiriAuthorizationRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.balance.MandiriGetBalanceRequest;
import com.imannuel.movin.intrabanktransferservice.dto.request.feign.mandiri.transfer.MandiriTransferIntraBankRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.mandiri.account_inquiry.MandiriInternalAccountInquiryResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.mandiri.authorization.MandiriAuthorizationResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.mandiri.balance.MandiriGetBalanceResponse;
import com.imannuel.movin.intrabanktransferservice.dto.response.feign.mandiri.transfer.MandiriTransferIntraBankResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "MandiriFeignClient", url = "${bank.mandiri.sandbox-url}")
public interface MandiriFeignClient {
    @PostMapping(
            value = "/openapi/auth/v2.0/access-token/b2b"
    )
    @ResponseBody
    MandiriAuthorizationResponse getAuthorizationToken(
            @RequestHeader("X-TIMESTAMP") String timestamp,
            @RequestHeader("X-CLIENT-KEY") String clientKey,
            @RequestHeader("X-SIGNATURE") String signature,
            @RequestBody MandiriAuthorizationRequest request
    );

    @PostMapping(
            value = "/openapi/customers/v2.1/balance-inquiry"
    )
    MandiriGetBalanceResponse getBalance(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "X-EXTERNAL-ID") String externalId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody MandiriGetBalanceRequest request
    );

    @PostMapping(
            value = "/openapi/customers/v2.0/account-inquiry-internal"
    )
    MandiriInternalAccountInquiryResponse getInternalAccountInquiry(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "X-EXTERNAL-ID") String externalId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody MandiriInternalAccountInquiryRequest request
    );

    @PostMapping(
            value = "/openapi/transactions/v2.0/transfer-intrabank"
    )
    MandiriTransferIntraBankResponse transferIntraBank(
            @RequestHeader(value = "X-TIMESTAMP") String timestamp,
            @RequestHeader(value = "X-SIGNATURE") String signature,
            @RequestHeader(value = "X-PARTNER-ID") String partnerId,
            @RequestHeader(value = "X-EXTERNAL-ID") String externalId,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestHeader(value = "CHANNEL-ID") String channelId,
            @RequestBody MandiriTransferIntraBankRequest request
    );
}
