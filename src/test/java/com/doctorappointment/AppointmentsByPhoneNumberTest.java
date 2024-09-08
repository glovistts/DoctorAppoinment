package com.doctorappointment;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.doctorappointment.dtos.PatientAppointmentResponseDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Patient;
import com.doctorappointment.exeption.PatientNotFoundException;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.repositories.PatientRepository;
import com.doctorappointment.service.AppointmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AppointmentsByPhoneNumberTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAppointmentsByPhoneNumber_NoAppointments() {
        String phoneNumber = "123456789";
        Patient patient = new Patient();
        patient.setName("Ali");
        patient.setPhoneNumber(phoneNumber);

        when(patientRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(patient));

        when(appointmentRepository.findByPatient(patient)).thenReturn(Collections.emptyList());

        List<PatientAppointmentResponseDto> result = appointmentService.getAppointmentsByPhoneNumber(phoneNumber);

        assertTrue(result.isEmpty());
        verify(patientRepository).findByPhoneNumber(phoneNumber);
        verify(appointmentRepository).findByPatient(patient);

        verifyNoMoreInteractions(patientRepository, appointmentRepository);
    }

    @Test
    public void testGetAppointmentsByPhoneNumber_MultipleAppointments() {
        String phoneNumber = "123456789";
        Patient patient = new Patient();
        patient.setPhoneNumber(phoneNumber);

        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        appointment1.setPatient(patient);

        Appointment appointment2 = new Appointment();
        appointment2.setId(2L);
        appointment2.setPatient(patient);

        List<Appointment> appointments = Arrays.asList(appointment1, appointment2);
        when(patientRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatient(patient)).thenReturn(appointments);

        List<PatientAppointmentResponseDto> result = appointmentService.getAppointmentsByPhoneNumber(phoneNumber);

        assertEquals(2, result.size());
        verify(patientRepository).findByPhoneNumber(phoneNumber);
        verify(appointmentRepository).findByPatient(patient);
    }

    @Test(expected = PatientNotFoundException.class)
    public void testGetAppointmentsByPhoneNumber_PatientNotFound() {
        String phoneNumber = "123456789";
        when(patientRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        appointmentService.getAppointmentsByPhoneNumber(phoneNumber);
    }
}
