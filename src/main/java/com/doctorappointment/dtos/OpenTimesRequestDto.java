package com.doctorappointment.dtos;

import com.doctorappointment.annotations.DoctorDayNotExists.DoctorDayNotExists;
import com.doctorappointment.annotations.DoctorExists.DoctorExists;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@DoctorDayNotExists
public class OpenTimesRequestDto {
    @NotNull(message = "Doctor ID cannot be null")
    @DoctorExists
    private Long doctorId;
    @Schema(type = "string", pattern = "HH:mm:ss", example = "08:30:00")
    private LocalTime startDate;
    @Schema(type = "string", pattern = "HH:mm:ss", example = "19:30:00")
    private LocalTime endDate;
    @NotNull(message = "Day cannot be null")
    @Future(message = "Day must be a future date")
    private LocalDate day;
}
