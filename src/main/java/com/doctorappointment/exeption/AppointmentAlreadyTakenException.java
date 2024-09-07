package com.doctorappointment.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AppointmentAlreadyTakenException extends RuntimeException {
    public AppointmentAlreadyTakenException(String message) {
        super(message);
    }
}
