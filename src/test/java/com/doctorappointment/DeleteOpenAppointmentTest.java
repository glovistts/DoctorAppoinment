package com.doctorappointment;


import com.doctorappointment.dtos.TakeAppointmentRequestDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Patient;
import com.doctorappointment.enums.Status;
import com.doctorappointment.exeption.AppointmentAlreadyTakenException;
import com.doctorappointment.exeption.AppointmentNotFoundException;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.service.AppointmentService;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DeleteOpenAppointmentTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    public void setup() {
        appointmentRepository.deleteAll();
    }

    @Test
    public void testConcurrentUpdateAndDeleteAppointment() throws InterruptedException {
        // Given
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setPatient(null);
        appointment.setStatus(Status.OPEN);  // Initially open
        appointment.setVersion(0L);  // Optimistic locking
        appointmentRepository.save(appointment);
        CountDownLatch latch = new CountDownLatch(1);
        // Simulate taking the appointment (Task 1)
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
