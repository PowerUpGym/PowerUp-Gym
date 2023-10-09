package com.example.PowerUpGym.Validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FullNameValidator implements ConstraintValidator<FullName, String> {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;

    @Override
    public void initialize(FullName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String fullName, ConstraintValidatorContext context) {
        if (fullName == null) {
            return false;
        }

        int length = fullName.length();
        return length >= MIN_LENGTH && length <= MAX_LENGTH;
    }
}