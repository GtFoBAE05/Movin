package com.imannuel.movin.intrabanktransferservice.controller;

import com.imannuel.movin.commonservice.controller.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Import(GlobalExceptionHandler.class)
public class ExceptionController {
}
