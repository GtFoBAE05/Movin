package com.imannuel.movin.intrabanktransferservice.configuration;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        String errorMessage = getErrorMessage(response);

        log.error("Feign error in method {}: status={}, message={}", methodKey, status, errorMessage);

        if (status.is4xxClientError()) {
            return new ResponseStatusException(status, errorMessage != null ? errorMessage : "Client Error (4xx)");
        }

        if (status.is5xxServerError()) {
            return new ResponseStatusException(status, errorMessage != null ? errorMessage : "Server Error (5xx)");
        }

        return new ResponseStatusException(status, "Feign Client Error");
    }

    private String getErrorMessage(Response response) {
        try {
            if (response.body() != null) {
                return new String(response.body().asInputStream().readAllBytes());
            }
        } catch (IOException e) {
            log.warn("Failed to read error response body", e);
        }
        return null;
    }
}