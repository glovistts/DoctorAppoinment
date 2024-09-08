package com.doctorappointment.entites;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.Set;

@Entity
@Table(name = "patient")
@Audited
@Getter
@Setter
public class Patient extends Base {
    private String name;
    private String phoneNumber;

    @OneToMany(mappedBy = "patient")
    private Set<Appointment> appointments;

    public Patient(String name, String phoneNumber) {
        this.name=name;
        this.phoneNumber=phoneNumber;
    }

    public Patient() {

    }
}
