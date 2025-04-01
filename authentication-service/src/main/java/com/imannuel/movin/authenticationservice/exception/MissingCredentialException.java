package com.imannuel.movin.authenticationservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingCredentialException extends RuntimeException{
    public MissingCredentialException(String message) {
        super(message);
    }
}
