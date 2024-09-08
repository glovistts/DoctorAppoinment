package com.doctorappointment.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TakeAppointmentRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    public TakeAppointmentRequestDto(String name, String phoneNumber) {
        this.name=name;
        this.phoneNumber=phoneNumber;
    }
}
