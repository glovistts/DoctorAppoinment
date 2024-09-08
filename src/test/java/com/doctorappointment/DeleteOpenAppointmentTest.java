package com.doctorappointment;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.doctorappointment.dtos.TakeAppointmentRequestDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Patient;
import com.doctorappointment.enums.Status;
import com.doctorappointment.exeption.AppointmentAlreadyTakenException;
import com.doctorappointment.exeption.AppointmentNotFoundException;
import com.doctorappointment.mappers.AppointmentMapper;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.repositories.DoctorRepository;
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

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DeleteOpenAppointmentTest {

    @Mock
    private AppointmentRepository appointmentRepository;  // Mocking the repository

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;  // Service where the repository will be injected

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    public void testConcurrentUpdateAndDeleteAppointment() throws InterruptedException {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStatus(Status.OPEN);
        appointment.setVersion(0L);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        CountDownLatch latch = new CountDownLatch(1);

        Runnable takeTask = () -> {
            TakeAppointmentRequestDto requestDto = new TakeAppointmentRequestDto("John Doe", "123456789");
            try {
                appointmentService.takeAppointment(appointmentId, requestDto);
                System.out.println("takeing started");
                latch.countDown();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };

        Runnable deleteTask = () -> {
            try {
                latch.await();
                appointmentService.deleteOpenAppointment(appointmentId);
                System.out.println("deleted started");
            } catch (AppointmentNotFoundException | AppointmentAlreadyTakenException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread thread1 = new Thread(takeTask);
        Thread thread2 = new Thread(deleteTask);

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        appointment.setStatus(Status.TAKEN);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        Optional<Appointment> updatedAppointment = appointmentRepository.findById(appointmentId);
        assertTrue(updatedAppointment.isPresent());
        assertEquals(Status.TAKEN, updatedAppointment.get().getStatus());

        verify(appointmentRepository, never()).delete(any(Appointment.class));
    }
    @Test
    public void testDeleteNonExistentAppointment() {
        Long appointmentId = 1L;
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.deleteOpenAppointment(appointmentId);
        });

        assertEquals("Appointment not found.", exception.getMessage());
    }

    @Test
    public void testDeleteTakenAppointment() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setPatient(new Patient()); // Simulate taken appointment
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        Exception exception = assertThrows(AppointmentAlreadyTakenException.class, () -> {
            appointmentService.deleteOpenAppointment(appointmentId);
        });

        assertEquals("Appointment is already taken by a patient.", exception.getMessage());
    }
}
