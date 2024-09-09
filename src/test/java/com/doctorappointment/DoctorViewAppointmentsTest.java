package com.doctorappointment;

import com.doctorappointment.dtos.AppointmentResponseDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Doctor;
import com.doctorappointment.entites.Patient;
import com.doctorappointment.enums.Status;
import com.doctorappointment.mappers.AppointmentMapper;
import com.doctorappointment.mappers.AppointmentResponseDtoMapper;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.repositories.DoctorRepository;
import com.doctorappointment.repositories.PatientRepository;
import com.doctorappointment.service.AppointmentService;
import com.doctorappointment.validator.AppointmentValidator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest // This loads the entire application context
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DoctorViewAppointmentsTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private AppointmentResponseDtoMapper appointmentResponseDtoMapper;

    @Autowired
    private AppointmentValidator appointmentValidator;

    @Autowired
    private AppointmentService appointmentService;

    @BeforeEach
    public void setUp() {
        // Clean the repository if needed, to avoid test data persistence between tests
        appointmentRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();
    }


    @Test
    public void testGetDoctorAppointments_WithAppointments() {

        Long doctorId = 1L;
        List<AppointmentResponseDto> resultEmpty = appointmentService.getDoctorAppointments(doctorId);
        assertEquals(0, resultEmpty.size(), "The result list should be empty.");

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setName("mr john");
        doctor.setSpecialization("something");
        doctorRepository.save(doctor); // Save doctor into the repository

        Appointment appointment1 = new Appointment();
        appointment1.setStartTime(LocalTime.of(9, 0));
        appointment1.setEndTime(LocalTime.of(9, 30));
        appointment1.setDay(LocalDate.of(2024, 12, 8));
        appointment1.setStatus(Status.TAKEN);
        appointment1.setDoctor(doctor); // Link the appointment to the saved doctor

        Patient patient = new Patient();
        patient.setName("ali");
        patient.setPhoneNumber("1234567890");
        patient=patientRepository.save(patient);

        appointment1.setPatient(patient);

        appointment1=appointmentRepository.save(appointment1);

        List<AppointmentResponseDto> result = appointmentService.getDoctorAppointments(doctorId);
        assertEquals(1, result.size(), "The result list should contain one appointment.");
        AppointmentResponseDto dto = result.get(0);
        assertEquals(appointment1.getId(), dto.getId());
        assertEquals(appointment1.getStartTime(), dto.getStartTime());
        assertEquals(appointment1.getEndTime(), dto.getEndTime());
        assertEquals(appointment1.getDay(), dto.getDay());
        assertEquals(patient.getName(), dto.getPatientName());
        assertEquals(patient.getPhoneNumber(), dto.getPatientPhone());
    }
}