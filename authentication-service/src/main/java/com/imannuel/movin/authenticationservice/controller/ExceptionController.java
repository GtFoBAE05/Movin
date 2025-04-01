package com.imannuel.movin.authenticationservice.controller;

import com.imannuel.movin.authenticationservice.exception.MissingCredentialException;
import com.imannuel.movin.authenticationservice.exception.UserAlreadyRegisteredException;
import com.imannuel.movin.authenticationservice.exception.UserNotFoundException;
import com.imannuel.movin.commonservice.controller.GlobalExceptionHandler;
import com.imannuel.movin.commonservice.dto.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Import(GlobalExceptionHandler.class)
public class ExceptionController {
    @ExceptionHandler({UserAlreadyRegisteredException.class})
    public ResponseEntity<?> handlingUserAlreadyRegisteredException(UserAlreadyRegisteredException e) {
        return ResponseMapper.toCommonResponse(false,
                HttpStatus.BAD_REQUEST, e.getMessage(), null
        );
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<?> handlingUserNotFoundException() {
        return ResponseMapper.toCommonResponse(false,
                HttpStatus.NOT_FOUND, "User not exists", null
        );
    }

    @ExceptionHandler({MissingCredentialException.class})
    public ResponseEntity<?> handlingMissingCredentialException() {
        return ResponseMapper.toCommonResponse(false,
                HttpStatus.BAD_REQUEST, "User credentials are not fully setup", null
        );
    }
}
