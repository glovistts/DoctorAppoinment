package com.doctorappointment.validator;

import com.doctorappointment.dtos.OpenTimesRequestDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.enums.Messages;
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
            throw new IllegalArgumentException(Messages.END_AFTER_START_TIME);
        }
    }
    public static void validateAppointmentExistence(Optional<Appointment> appointmentOpt){
        if (appointmentOpt.isEmpty()) {
            throw new AppointmentNotFoundException(Messages.APPOINMETN_NOT_FOUND);
        }
    }
    public static void validateAppointmentPatientExistence(Appointment appointment){
        if (appointment.getPatient() != null) {
            throw new AppointmentAlreadyTakenException(Messages.APPOINTMENT_ALREADY_TAKEN);
        }
    }

    public static void validateAppointmentStatusTaken(Appointment appointment){
        if (appointment.getStatus() != Status.OPEN) {
            throw new AppointmentAlreadyTakenException(Messages.APPOINTMENT_ALREADY_TAKEN_DELETED);
        }
    }

}