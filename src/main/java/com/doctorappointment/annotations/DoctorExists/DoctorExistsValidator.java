package com.doctorappointment.annotations.DoctorExists;

import com.doctorappointment.repositories.DoctorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class DoctorExistsValidator implements ConstraintValidator<DoctorExists, Long> {
    private final DoctorRepository doctorRepository;
    public DoctorExistsValidator(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    @Override
    public boolean isValid(Long doctorId, ConstraintValidatorContext context) {
        return doctorId != null && doctorRepository.existsById(doctorId);
    }
}
