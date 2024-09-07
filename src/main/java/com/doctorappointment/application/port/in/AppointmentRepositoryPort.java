package com.doctorappointment.application.port.in;

import com.doctorappointment.domain.model.AppointmentEntity;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface AppointmentRepositoryPort {
    void save(AppointmentEntity appointmentEntity);
    List<AppointmentEntity> findOpenAppointments(String doctorId);
    AppointmentEntity findById(Long id);
    void deleteById(Long id);
}
