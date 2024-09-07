package com.doctorappointment.annotations.DoctorExists;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DoctorExistsValidator.class)
public @interface DoctorExists {

    String message() default "Doctor does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}