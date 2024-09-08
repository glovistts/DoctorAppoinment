package com.doctorappointment;

import com.doctorappointment.dtos.AppointmentResponseDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Patient;
import com.doctorappointment.enums.Status;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.service.AppointmentService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql")
public class DoctorViewAppointmentsTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDoctorAppointments_NoAppointments() {
        // Given
        Long doctorId = 1L;
        when(appointmentRepository.findByDoctorId(doctorId)).thenReturn(new ArrayList<>());

        // When
        List<AppointmentResponseDto> result = appointmentService.getDoctorAppointments(doctorId);

        // Then
        assertEquals(0, result.size(), "The result list should be empty.");
    }

    @Test
    public void testGetDoctorAppointments_WithAppointments() {
        Long doctorId = 1L;
        List<Appointment> appointments = new ArrayList<>();

        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        appointment1.setStartTime(LocalTime.of(9, 0));
        appointment1.setEndTime(LocalTime.of(9, 30));
        appointment1.setDay(LocalDate.of(2024, 9, 8));
        appointment1.setStatus(Status.TAKEN);

        Patient patient = new Patient();
        patient.setName("ali");
        patient.setPhoneNumber("1234567890");
        appointment1.setPatient(patient);

        appointments.add(appointment1);

        when(appointmentRepository.findByDoctorId(doctorId)).thenReturn(appointments);
        List<AppointmentResponseDto> result = appointmentService.getDoctorAppointments(doctorId);

        assertEquals(1, result.size(), "The result list should contain one appointment.");
        AppointmentResponseDto dto = result.get(0);
        assertEquals(1L, dto.getId());
        assertEquals(LocalTime.of(9, 0), dto.getStartTime());
        assertEquals(LocalTime.of(9, 30), dto.getEndTime());
        assertEquals(LocalDate.of(2024, 9, 8), dto.getDay());
        assertEquals(Status.TAKEN, dto.getStatus());
        assertEquals("ali", dto.getPatientName());
        assertEquals("1234567890", dto.getPatientPhone());
    }
}