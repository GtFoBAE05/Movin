package com.imannuel.movin.intrabanktransferservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imannuel.movin.commonservice.utility.ValidationUtility;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public ValidationUtility validationUtility(Validator validator) {
        return new ValidationUtility(validator);
    }
}

