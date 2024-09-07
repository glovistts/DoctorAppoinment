package com.doctorappointment.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.Set;

@Entity
@Table(name = "patient",schema = "doctor_schema")
@Audited
@Getter
@Setter
public class PatientEntity extends BaseEntity{
    private String name;
    private String phoneNumber;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private Set<AppointmentEntity> appointments;
}
