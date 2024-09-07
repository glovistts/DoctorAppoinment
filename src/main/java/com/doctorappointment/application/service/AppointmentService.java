package com.doctorappointment.application.service;

import com.doctorappointment.application.port.in.AppointmentRepositoryPort;
import com.doctorappointment.domain.service.AppointmentDomainService;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final AppointmentDomainService appointmentDomainService;
    private final AppointmentRepositoryPort appointmentRepositoryPort;

    public AppointmentService(AppointmentDomainService appointmentDomainService, AppointmentRepositoryPort appointmentRepositoryPort) {
        this.appointmentDomainService = appointmentDomainService;
        this.appointmentRepositoryPort = appointmentRepositoryPort;
    }

    public void addOpenTimes(String doctorId, String startDateTime, String endDateTime) {
        appointmentDomainService.addOpenTimes(doctorId, startDateTime, endDateTime);
    }
}