package com.doctorappointment.annotations.DoctorDayNotExists;

import com.doctorappointment.dtos.OpenTimesRequestDto;
import com.doctorappointment.repositories.AppointmentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class DoctorDayNotExistsValidator implements ConstraintValidator<DoctorDayNotExists, OpenTimesRequestDto> {

    private final AppointmentRepository appointmentRepository;

    public DoctorDayNotExistsValidator(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public boolean isValid(OpenTimesRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getDoctorId() == null || dto.getDay() == null) {
            return true;
        }
        return !appointmentRepository.existsByDoctorIdAndDay(dto.getDoctorId(), dto.getDay());
    }
}