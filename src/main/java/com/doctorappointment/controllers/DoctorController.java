package com.doctorappointment.controllers;

import com.doctorappointment.entites.Appointment;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping("/{doctorId/appointments")
    public ResponseEntity<String> addOpenTimes(@PathVariable Long doctorId,
                                               @RequestParam LocalDateTime startDate,
                                               @RequestParam LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().body("End date must be after start date.");
        }

        // Logic to break down time into 30-minute slots
        // and add appointments

        return ResponseEntity.ok("Open times added.");
    }

    @GetMapping("/{doctorId/appointments")
    public ResponseEntity<List<Appointment>> viewAppointments(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndStatus(doctorId, "OPEN");
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping("/{doctorId/appointments/{appointmentId}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long doctorId,
                                                    @PathVariable Long appointmentId) {
        // Check if appointment is open
        // Handle deletion and concurrency

        return ResponseEntity.ok("Appointment deleted.");
    }
}