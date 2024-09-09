package com.doctorappointment.dtos;

import com.doctorappointment.enums.Messages;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TakeAppointmentRequestDto {
    @NotBlank(message = Messages.NAME_REQUIRED)
    private String name;
    @NotBlank(message = Messages.PHONE_NUMBER_REQUIRED)
    private String phoneNumber;
    public TakeAppointmentRequestDto(String name, String phoneNumber) {
        this.name=name;
        this.phoneNumber=phoneNumber;
    }
    public TakeAppointmentRequestDto() {
    }
}
