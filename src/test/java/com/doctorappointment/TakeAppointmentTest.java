package com.doctorappointment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


import com.doctorappointment.dtos.TakeAppointmentRequestDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Patient;
import com.doctorappointment.enums.Status;
import com.doctorappointment.exeption.AppointmentAlreadyTakenException;
import com.doctorappointment.exeption.AppointmentNotFoundException;
import com.doctorappointment.mappers.AppointmentMapper;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.repositories.PatientRepository;
import com.doctorappointment.service.AppointmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class TakeAppointmentTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTakeAppointment_Success() {
        Long appointmentId = 1L;
        TakeAppointmentRequestDto requestDto = new TakeAppointmentRequestDto("John Doe", "123456789");
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStatus(Status.OPEN);

        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setPhoneNumber("123456789");

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(patientRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        appointmentService.takeAppointment(appointmentId, requestDto);

        assertEquals(Status.TAKEN, appointment.getStatus());
        assertEquals(patient, appointment.getPatient());
        verify(appointmentRepository).save(appointment);
    }

    @Test(expected = AppointmentNotFoundException.class)
    public void testTakeAppointment_AppointmentNotFound() {
        Long appointmentId = 1L;
        TakeAppointmentRequestDto requestDto = new TakeAppointmentRequestDto("John Doe", "123456789");

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        appointmentService.takeAppointment(appointmentId, requestDto);
    }

    @Test(expected = AppointmentAlreadyTakenException.class)
    public void testTakeAppointment_AppointmentAlreadyTaken() {
        Long appointmentId = 1L;
        TakeAppointmentRequestDto requestDto = new TakeAppointmentRequestDto("John Doe", "123456789");
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStatus(Status.TAKEN);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        appointmentService.takeAppointment(appointmentId, requestDto);
    }

    @Test(expected = AppointmentNotFoundException.class)
    public void testTakeAppointment_MissingName() {
        Long appointmentId = 1L;
        TakeAppointmentRequestDto requestDto = new TakeAppointmentRequestDto(null, "123456789");

        appointmentService.takeAppointment(appointmentId, requestDto);
    }

    @Test(expected = AppointmentNotFoundException.class)
    public void testTakeAppointment_MissingPhoneNumber() {
        Long appointmentId = 1L;
        TakeAppointmentRequestDto requestDto = new TakeAppointmentRequestDto("John Doe", null);

        appointmentService.takeAppointment(appointmentId, requestDto);
    }

}
