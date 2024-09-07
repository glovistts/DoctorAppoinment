package com.doctorappointment.controllers;

import com.doctorappointment.dtos.OpenTimesRequestDto;
import com.doctorappointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix.v1}/Doctor")
public class DoctorController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/{doctorId}/open-times")
    public ResponseEntity<String> addOpenTimes(@PathVariable Long doctorId, @RequestBody OpenTimesRequestDto dto) {
        dto.setDoctorId(doctorId);
        try {
            appointmentService.addOpenTimes(dto);
            return ResponseEntity.ok("Open times added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
