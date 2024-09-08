package com.doctorappointment.dtos;

import com.doctorappointment.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientAppointmentResponseDto {
    private Long appointmentId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String doctorName;
    private LocalDate day;
    private Status status;

    public PatientAppointmentResponseDto(Long appointmentId, LocalTime startTime, LocalTime endTime, String doctorName, LocalDate day, Status status) {
        this.appointmentId = appointmentId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctorName = doctorName;
        this.day = day;
        this.status = status;
    }
}
