package com.doctorappointment.service;

import com.doctorappointment.dtos.OpenTimesRequestDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Doctor;
import com.doctorappointment.mappers.AppointmentMapper;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Transactional
    public void addOpenTimes(OpenTimesRequestDto dto) {
        LocalDateTime start = dto.getStartDate();
        LocalDateTime end = dto.getEndDate();
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        List<Appointment> appointments = appointmentMapper.mapToAppointments(dto, doctor);

        appointmentRepository.saveAll(appointments);
    }


}