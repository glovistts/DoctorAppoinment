package com.doctorappointment.mappers;

import com.doctorappointment.dtos.AppointmentResponseDto;
import com.doctorappointment.dtos.OpenTimesRequestDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Doctor;
import com.doctorappointment.enums.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppointmentMapper {
    public List<Appointment> mapToAppointments(OpenTimesRequestDto dto, Doctor doctor) {
        List<Appointment> appointments = new ArrayList<>();
        LocalTime start = dto.getStartDate();
        LocalTime end = dto.getEndDate();
        LocalDate day=dto.getDay();

        while (!start.isAfter(end.minusMinutes(30))) {
            Appointment appointment = new Appointment();
            appointment.setStartTime(start);
            appointment.setEndTime(start.plusMinutes(30));
            appointment.setDoctor(doctor);
            appointment.setDay(day);
            appointment.setStatus(Status.OPEN);
            appointments.add(appointment);
            start = start.plusMinutes(30);
        }
        return appointments;
    }

    public AppointmentResponseDto toDto(Appointment appointment) {
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setId(appointment.getId());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setDay(appointment.getDay());
        return dto;
    }
}
