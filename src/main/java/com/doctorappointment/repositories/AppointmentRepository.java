package com.doctorappointment.repositories;

import com.doctorappointment.entites.Appointment;
import com.doctorappointment.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByDoctorIdAndDay(Long doctorId, LocalDate day);
    List<Appointment> findByDoctorId(Long doctorId);
    Optional<Appointment> findByAppointmentId( Long appointmentId);
    List<Appointment> findByDoctorIdAndDayAndPatientIsNull(Long doctorId, LocalDate day);
}