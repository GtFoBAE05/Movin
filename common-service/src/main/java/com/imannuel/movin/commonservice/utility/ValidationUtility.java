package com.imannuel.movin.commonservice.utility;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.Set;

public class ValidationUtility {
    private final Validator validator;

    public ValidationUtility(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object o) {
        Set<ConstraintViolation<Object>> violations = validator.validate(o);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
