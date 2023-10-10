package com.example.PowerUpGym.Validation.validAge;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class AgeValidator implements ConstraintValidator<AgeGreaterThanZero, Integer> {
    @Override
    public void initialize(AgeGreaterThanZero constraintAnnotation) {
    }
    @Override
    public boolean isValid(Integer age, ConstraintValidatorContext context) {
        return age != null && age > 0;
    }
}