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
import com.doctorappointment.mappers.AppointmentResponseDtoMapper;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.repositories.DoctorRepository;
import com.doctorappointment.repositories.PatientRepository;
import com.doctorappointment.validator.AppointmentValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;

    private AppointmentMapper appointmentMapper;
    private AppointmentResponseDtoMapper appointmentResponseDtoMapper;
    private AppointmentValidator appointmentValidator;
    AppointmentService(
       AppointmentRepository appointmentRepository,
       PatientRepository patientRepository,
       DoctorRepository doctorRepository,
       AppointmentMapper appointmentMapper,
       AppointmentResponseDtoMapper appointmentResponseDtoMapper,
       AppointmentValidator appointmentValidator
    ){
        this.appointmentRepository=appointmentRepository;
        this.patientRepository=patientRepository;
        this.doctorRepository=doctorRepository;
        this.appointmentMapper = appointmentMapper;
        this.appointmentResponseDtoMapper=appointmentResponseDtoMapper;
        this.appointmentValidator=appointmentValidator;
    }

    @Transactional
    public List<Appointment> addOpenTimes(OpenTimesRequestDto dto) {
        AppointmentValidator.validateOpenTimes(dto);
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("دکتر پیدا نشد"));
        List<Appointment> appointments = appointmentMapper.mapToAppointments(dto, doctor);
        appointmentRepository.saveAll(appointments);
        return appointments;
    }
    public List<AppointmentResponseDto> getDoctorAppointments(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointmentResponseDtoMapper.toDtoList(appointments);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteOpenAppointment(Long appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        AppointmentValidator.validateAppointmentExistence(appointmentOpt);
        Appointment appointment = appointmentOpt.get();
        AppointmentValidator.validateAppointmentPatientExistence(appointment);
        appointmentRepository.delete(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getOpenAppointments(Long doctorId, LocalDate day) {
        List<Appointment> openAppointments = appointmentRepository.findByDoctorIdAndDayAndPatientIsNull(doctorId, day);
        return openAppointments.stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void takeAppointment(Long appointmentId, TakeAppointmentRequestDto requestDto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        AppointmentValidator.validateAppointmentStatusTaken(appointment);
        Patient patient = patientRepository.findByPhoneNumber(requestDto.getPhoneNumber())
                .orElse(new Patient(requestDto.getName(), requestDto.getPhoneNumber()));
        appointmentRepository.save(appointmentMapper.patientToAppointment(appointment,patient));
    }
    public List<PatientAppointmentResponseDto> getAppointmentsByPhoneNumber(String phoneNumber) {
        Patient patient = patientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new PatientNotFoundException("No patient found with the provided phone number"));
        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        return appointments.stream()
                .map(AppointmentMapper::convertToDto)
                .collect(Collectors.toList());
    }


}