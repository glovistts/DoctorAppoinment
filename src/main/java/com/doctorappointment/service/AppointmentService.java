package com.doctorappointment.service;

import com.doctorappointment.dtos.AppointmentResponseDto;
import com.doctorappointment.dtos.OpenTimesRequestDto;
import com.doctorappointment.dtos.PatientAppointmentResponseDto;
import com.doctorappointment.dtos.TakeAppointmentRequestDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Doctor;
import com.doctorappointment.entites.Patient;
import com.doctorappointment.enums.Status;
import com.doctorappointment.exeption.AppointmentAlreadyTakenException;
import com.doctorappointment.exeption.AppointmentNotFoundException;
import com.doctorappointment.exeption.PatientNotFoundException;
import com.doctorappointment.mappers.AppointmentMapper;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.repositories.DoctorRepository;
import com.doctorappointment.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Transactional
    public List<Appointment> addOpenTimes(OpenTimesRequestDto dto) {
        LocalTime start = dto.getStartDate();
        LocalTime end = dto.getEndDate();
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("زمان پایان باید بعد از زمان شروع باشد");
        }

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("دکتر پیدا نشد"));

        List<Appointment> appointments = appointmentMapper.mapToAppointments(dto, doctor);

        appointmentRepository.saveAll(appointments);
        return appointments;
    }
    public List<AppointmentResponseDto> getDoctorAppointments(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        List<AppointmentResponseDto> responseList = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentResponseDto dto = new AppointmentResponseDto();
            dto.setId(appointment.getId());
            dto.setStartTime(appointment.getStartTime());
            dto.setEndTime(appointment.getEndTime());
            dto.setDay(appointment.getDay());
            dto.setStatus(appointment.getStatus());
            if (appointment.getPatient() != null) {
                dto.setPatientName(appointment.getPatient().getName());
                dto.setPatientPhone(appointment.getPatient().getPhoneNumber());
            }

            responseList.add(dto);
        }
        return responseList;
    }

    @Transactional
    public void deleteOpenAppointment(Long appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new AppointmentNotFoundException("Appointment not found.");
        }
        Appointment appointment = appointmentOpt.get();
        if (appointment.getPatient() != null) {
            throw new AppointmentAlreadyTakenException("Appointment is already taken by a patient.");
        }
        appointmentRepository.delete(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getOpenAppointments(Long doctorId, LocalDate day) {
        List<Appointment> openAppointments = appointmentRepository.findByDoctorIdAndDayAndPatientIsNull(doctorId, day);
        return openAppointments.stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void takeAppointment(Long appointmentId, TakeAppointmentRequestDto requestDto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        if (appointment.getStatus() != Status.OPEN) {
            throw new AppointmentAlreadyTakenException("Appointment is already taken or deleted");
        }
        Patient patient = patientRepository.findByPhoneNumber(requestDto.getPhoneNumber())
                .orElse(new Patient(requestDto.getName(), requestDto.getPhoneNumber()));
        appointment.setPatient(patient);
        appointment.setStatus(Status.TAKEN);
        appointmentRepository.save(appointment);
    }
    public List<PatientAppointmentResponseDto> getAppointmentsByPhoneNumber(String phoneNumber) {
        Patient patient = patientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new PatientNotFoundException("No patient found with the provided phone number"));
        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PatientAppointmentResponseDto convertToDto(Appointment appointment) {
        return new PatientAppointmentResponseDto(
                appointment.getId(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getDoctor().getName(),
                appointment.getDay(),
                appointment.getStatus()
        );
    }
}