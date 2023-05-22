package com.formedix.dmedelacruz.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankPositiveValidator implements ConstraintValidator<NotBlankPositive, Double> {
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }
        return value >= 0;
    }
}
