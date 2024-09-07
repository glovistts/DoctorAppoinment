package com.doctorappointment.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment",schema = "doctor_schema")
@Audited
@Getter
@Setter
public class AppointmentEntity extends BaseEntity{
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Column(name = "is_taken")
    private boolean isTaken;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

}
