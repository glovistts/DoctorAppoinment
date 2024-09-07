package com.doctorappointment.controllers;

import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Patient;
import com.doctorappointment.repositories.AppointmentRepository;
import com.doctorappointment.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/{doctorId/appointments")
    public ResponseEntity<List<Appointment>> viewAvailableAppointments(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndStatus(doctorId, "OPEN");
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/appointments")
    public ResponseEntity<String> takeAppointment(@RequestParam Long appointmentId,
                                                  @RequestParam String name,
                                                  @RequestParam String phoneNumber) {
        // Validate input
        if (name.isEmpty() || phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body("Name and phone number are required.");
        }

        // Handle appointment taking and concurrency

        return ResponseEntity.ok("Appointment taken.");
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> viewMyAppointments(@RequestParam String phoneNumber) {
        Patient patient = patientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        List<Appointment> appointments = appointmentRepository.findByPatientId(patient.getId());
        return ResponseEntity.ok(appointments);
    }
}