package com.doctorappointment.enums;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE_STATUS("ACTIVE_STATUS"),
    DEACTIVE_STATUS("DEACTIVE_STATUS");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
