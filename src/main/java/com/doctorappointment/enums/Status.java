package com.doctorappointment.enums;

import lombok.Getter;

@Getter
public enum Status {
    OPEN("OPEN"),
    TAKEN("TAKEN");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
