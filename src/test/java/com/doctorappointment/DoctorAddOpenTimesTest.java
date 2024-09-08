package com.doctorappointment;

import com.doctorappointment.dtos.OpenTimesRequestDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Doctor;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.repositories.DoctorRepository;
import com.doctorappointment.service.AppointmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DoctorAddOpenTimesTest {

    @Autowired
    private AppointmentService appointmentService;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private DoctorRepository doctorRepository;

    @Test
    public void whenEndDateIsBeforeStartDate_thenThrowException() {
        OpenTimesRequestDto requestDto = new OpenTimesRequestDto();
        requestDto.setDoctorId(1L);
        requestDto.setDay(LocalDate.now().plusDays(1));
        requestDto.setStartDate(LocalTime.of(10, 0));
        requestDto.setEndDate(LocalTime.of(9, 0)); // End time before start time

        // Test for exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.addOpenTimes(requestDto);
        });

        String expectedMessage = "زمان پایان باید بعد از زمان شروع باشد";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenPeriodIsLessThan30Minutes_thenNoAppointmentShouldBeAdded() {
        OpenTimesRequestDto requestDto = new OpenTimesRequestDto();
        requestDto.setDoctorId(1L);
        requestDto.setDay(LocalDate.now());
        requestDto.setStartDate(LocalTime.of(10, 0));
        requestDto.setEndDate(LocalTime.of(10, 20)); // Less than 30-minute period

        // Mock the doctor entity
        Doctor doctor = new Doctor();
        doctor.setId(1L);

        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // Call the service
        List<Appointment> appointments = appointmentService.addOpenTimes(requestDto);

        // Assert no appointments were added
        assertTrue(appointments.isEmpty());
    }

    @Test
    public void whenValidTimeRange_thenAppointmentsShouldBeAdded() {
        OpenTimesRequestDto requestDto = new OpenTimesRequestDto();
        requestDto.setDoctorId(1L);
        requestDto.setDay(LocalDate.now());
        requestDto.setStartDate(LocalTime.of(10, 0));
        requestDto.setEndDate(LocalTime.of(12, 0)); // Two hours, valid for four 30-minute slots

        // Mock the doctor entity
        Doctor doctor = new Doctor();
        doctor.setId(1L);

        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // Call the service
        List<Appointment> appointments = appointmentService.addOpenTimes(requestDto);

        // Assert that 4 appointments were added
        assertEquals(4, appointments.size());
        assertEquals(LocalTime.of(10, 0), appointments.get(0).getStartTime());
        assertEquals(LocalTime.of(10, 30), appointments.get(1).getStartTime());
        assertEquals(LocalTime.of(11, 0), appointments.get(2).getStartTime());
        assertEquals(LocalTime.of(11, 30), appointments.get(3).getStartTime());
    }
}