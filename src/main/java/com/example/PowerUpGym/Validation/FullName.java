package com.example.PowerUpGym.Validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FullNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FullName {
    String message() default "Full name must not be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
