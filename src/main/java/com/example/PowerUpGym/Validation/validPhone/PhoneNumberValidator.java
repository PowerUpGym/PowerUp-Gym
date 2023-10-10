package com.example.PowerUpGym.Validation.validPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final int MIN_LENGTH = 10;
    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
    }
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return false;
        }
        return phoneNumber.length() >= MIN_LENGTH;
    }
}