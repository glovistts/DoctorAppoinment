package com.doctorappointment.mappers;

import com.doctorappointment.dtos.OpenTimesRequestDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Doctor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppointmentMapper {
    public List<Appointment> mapToAppointments(OpenTimesRequestDto dto, Doctor doctor) {
        List<Appointment> appointments = new ArrayList<>();
        LocalDateTime start = dto.getStartDate();
        LocalDateTime end = dto.getEndDate();
        LocalDate day=dto.getDay();

        while (start.plusMinutes(30).isBefore(end) || start.plusMinutes(30).isEqual(end)) {
            Appointment appointment = new Appointment();
            appointment.setStartTime(start);
            appointment.setEndTime(start.plusMinutes(30));
            appointment.setDoctor(doctor);
            appointment.setDay(day);
            appointments.add(appointment);
            start = start.plusMinutes(30);
        }
        return appointments;
    }
}
