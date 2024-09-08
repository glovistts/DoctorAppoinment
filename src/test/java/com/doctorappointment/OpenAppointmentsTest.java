package com.doctorappointment;

import com.doctorappointment.dtos.AppointmentResponseDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.mappers.AppointmentMapper;
import com.doctorappointment.repositories.AppointmentRepository;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class OpenAppointmentsTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOpenAppointments_NoAppointments() {
        Long doctorId = 1L;
        LocalDate day = LocalDate.of(2024, 9, 8);

        when(appointmentRepository.findByDoctorIdAndDayAndPatientIsNull(doctorId, day))
                .thenReturn(Collections.emptyList());

        when(appointmentMapper.toDto(any(Appointment.class)))
                .thenAnswer(invocation -> null);
        List<AppointmentResponseDto> result = appointmentService.getOpenAppointments(doctorId, day);
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Ensure the result is an empty list
    }
}
