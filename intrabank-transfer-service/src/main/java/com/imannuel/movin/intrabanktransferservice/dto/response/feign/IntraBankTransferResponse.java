package com.imannuel.movin.intrabanktransferservice.dto.response.feign;

public interface IntraBankTransferResponse {
    boolean isSuccessfully();

    String getMessage();
}