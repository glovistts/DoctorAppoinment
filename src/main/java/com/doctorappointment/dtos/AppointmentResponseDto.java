package com.doctorappointment.dtos;

import com.doctorappointment.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentResponseDto {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate day;
    private Status status;
    private String patientName;
    private String patientPhone;
}