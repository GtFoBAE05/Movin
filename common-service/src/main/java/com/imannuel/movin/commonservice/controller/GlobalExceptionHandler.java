package com.imannuel.movin.commonservice.controller;

import com.imannuel.movin.commonservice.dto.mapper.ResponseMapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handlingConstraintViolationException(ConstraintViolationException e) {
        return ResponseMapper.toCommonResponse(false,
                HttpStatus.BAD_REQUEST, e.getConstraintViolations().toString(), null
        );
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> handlingResponseStatusException(ResponseStatusException e) {
        return ResponseMapper.toCommonResponse(false,
                HttpStatus.valueOf(e.getStatusCode().value()), e.getReason(), null
        );
    }
}
