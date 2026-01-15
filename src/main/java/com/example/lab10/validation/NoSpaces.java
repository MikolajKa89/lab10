package com.example.lab10.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoSpacesValidator.class)
public @interface NoSpaces {
    String message() default "Pole nie moze zawierac spacji";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}