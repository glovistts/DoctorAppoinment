package com.doctorappointment.domain.service;

import com.doctorappointment.application.port.in.AppointmentRepositoryPort;
import com.doctorappointment.domain.model.AppointmentEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentDomainService {

    private final AppointmentRepositoryPort appointmentRepositoryPort;

    public AppointmentDomainService(AppointmentRepositoryPort appointmentRepositoryPort) {
        this.appointmentRepositoryPort = appointmentRepositoryPort;
    }

    public void addOpenTimes(String doctorId, String startDateTime, String endDateTime) {
        LocalDateTime start = LocalDateTime.parse(startDateTime);
        LocalDateTime end = LocalDateTime.parse(endDateTime);

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        Duration duration = Duration.between(start, end);
        if (duration.toMinutes() < 30) {
            return;
        }
        while (duration.toMinutes() >= 30) {
            AppointmentEntity appointment = new AppointmentEntity();
            appointment.setDoctor(doctorId);
            appointment.setStartTime(start);
            appointment.setEndTime(start.plusMinutes(30));
            appointment.setTaken(false);
            appointmentRepositoryPort.save(appointment);

            start = start.plusMinutes(30);
            duration = Duration.between(start, end);
        }
    }

    public List<AppointmentEntity> getOpenAppointments(String doctorId) {
        return appointmentRepositoryPort.findOpenAppointments(doctorId);
    }

    public void deleteAppointment(Long id) {
        AppointmentEntity appointment = appointmentRepositoryPort.findById(id);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }
        if (appointment.isTaken()) {
            throw new IllegalStateException("Cannot delete a taken appointment");
        }
        appointmentRepositoryPort.deleteById(id);
    }
}