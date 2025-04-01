package com.imannuel.movin.commonservice.dto.mapper;

import com.imannuel.movin.commonservice.dto.response.template.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseMapper {
    public static <T> ResponseEntity<?> toCommonResponse(Boolean success, HttpStatus httpStatus, String message, T data) {
        return ResponseEntity.status(httpStatus).body(
                CommonResponse.builder()
                        .success(success)
                        .message(message)
                        .data(data)
                        .pagination(null)
                        .build()
        );
    }
}
