package com.doctorappointment.validator;

import com.doctorappointment.dtos.OpenTimesRequestDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.enums.Status;
import com.doctorappointment.exeption.AppointmentAlreadyTakenException;
import com.doctorappointment.exeption.AppointmentNotFoundException;
import com.doctorappointment.repositories.AppointmentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppointmentValidator {
    private final AppointmentRepository appointmentRepository;

    public AppointmentValidator(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public static void validateOpenTimes(OpenTimesRequestDto dto) {
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("زمان پایان باید بعد از زمان شروع باشد");
        }
    }
    public static void validateAppointmentExistence(Optional<Appointment> appointmentOpt){
        if (appointmentOpt.isEmpty()) {
            throw new AppointmentNotFoundException("Appointment not found.");
        }
    }
    public static void validateAppointmentPatientExistence(Appointment appointment){
        if (appointment.getPatient() != null) {
            throw new AppointmentAlreadyTakenException("Appointment is already taken by a patient.");
        }
    }

    public static void validateAppointmentStatusTaken(Appointment appointment){
        if (appointment.getStatus() != Status.OPEN) {
            throw new AppointmentAlreadyTakenException("Appointment is already taken or deleted");
        }
    }

}