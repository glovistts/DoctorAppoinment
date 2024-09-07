package com.doctorappointment.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class OpenTimesRequestDto {
    private Long doctorId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDate day;
}
