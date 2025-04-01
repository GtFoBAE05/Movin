package org.imannuel.movin.userservice.controller;

import com.imannuel.movin.commonservice.controller.GlobalExceptionHandler;
import com.imannuel.movin.commonservice.dto.mapper.ResponseMapper;
import org.springframework.context.annotation.Import;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Import(GlobalExceptionHandler.class)
public class ExceptionController {
    @ExceptionHandler({PropertyReferenceException.class})
    public ResponseEntity<?> handlingPropertyReferenceException(PropertyReferenceException e) {
        return ResponseMapper.toCommonResponse(false,
                HttpStatus.BAD_REQUEST, String.format("%s is invalid field to sort", e.getPropertyName()),
                null
        );
    }
}
