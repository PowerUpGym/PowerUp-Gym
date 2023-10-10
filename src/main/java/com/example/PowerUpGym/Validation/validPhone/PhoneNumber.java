package com.example.PowerUpGym.Validation.validPhone;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String message() default "Phone number must be at least 10 characters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}