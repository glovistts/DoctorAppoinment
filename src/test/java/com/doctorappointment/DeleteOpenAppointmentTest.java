package com.doctorappointment;


import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Patient;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql")
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
    @Transactional
    public void testDeleteOpenAppointment_ConcurrentModification() throws InterruptedException {
        // Given
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setPatient(null);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        Runnable deleteTask = () -> {
            try {
                appointmentService.deleteOpenAppointment(appointmentId);
            } catch (AppointmentNotFoundException | AppointmentAlreadyTakenException e) {
                System.out.println(e);
            }
        };

        Thread thread1 = new Thread(deleteTask);
        Thread thread2 = new Thread(deleteTask);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        // Verify that the repository delete method is called only once
        verify(appointmentRepository, times(1)).delete(any(Appointment.class));
    }

    @Test
    @Transactional
    public void testDeleteNonExistentAppointment() {
        Long appointmentId = 1L;
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.deleteOpenAppointment(appointmentId);
        });

        assertEquals("Appointment not found.", exception.getMessage());
    }

    @Test
    @Transactional
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
