package com.doctorappointment.entites;

import com.doctorappointment.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
@Audited
@Getter
@Setter
public class Appointment extends Base {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "appointment_day")
    private LocalDate day;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
