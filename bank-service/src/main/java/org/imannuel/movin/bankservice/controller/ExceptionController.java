package org.imannuel.movin.bankservice.controller;

import com.imannuel.movin.commonservice.controller.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Import(GlobalExceptionHandler.class)
public class ExceptionController {
}
