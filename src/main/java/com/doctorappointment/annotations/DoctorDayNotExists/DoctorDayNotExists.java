package com.doctorappointment.annotations.DoctorDayNotExists;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DoctorDayNotExistsValidator.class)
public @interface DoctorDayNotExists {

    String message() default "Doctor already has appointments on this day";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}