package com.doctorappointment.controllers;

import com.doctorappointment.dtos.AppointmentResponseDto;
import com.doctorappointment.dtos.OpenTimesRequestDto;
import com.doctorappointment.dtos.PatientAppointmentResponseDto;
import com.doctorappointment.dtos.TakeAppointmentRequestDto;
import com.doctorappointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${apiPrefix.v1}/Appointment")
public class AppointmentConrtoller {
    private final AppointmentService appointmentService;

    public AppointmentConrtoller(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/doctor/{doctorId}")
    public List<AppointmentResponseDto> getDoctorAppointments(@PathVariable Long doctorId) {
        return appointmentService.getDoctorAppointments(doctorId);
    }
    @GetMapping("/doctor/{doctorId}/open-appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getOpenAppointments(
            @PathVariable Long doctorId,
            @RequestParam LocalDate day) {
        List<AppointmentResponseDto> openAppointments = appointmentService.getOpenAppointments(doctorId, day);
        return ResponseEntity.ok(openAppointments);
    }
    @GetMapping("/patient/{phoneNumber}")
    public ResponseEntity<List<PatientAppointmentResponseDto>> getAppointmentsByPhoneNumber(
            @PathVariable String phoneNumber) {
        List<PatientAppointmentResponseDto> appointments = appointmentService.getAppointmentsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(appointments);
    }
    @DeleteMapping("/doctor/delete/{appointmentId}")
    public ResponseEntity<String> deleteOpenAppointment( @PathVariable Long appointmentId) {
        appointmentService.deleteOpenAppointment(appointmentId);
        return ResponseEntity.ok("نوبت با موفقیت حذف شد.");
    }
    @PostMapping("/open-times")
    public ResponseEntity<String> addOpenTimes(@Valid @RequestBody OpenTimesRequestDto dto) {
            appointmentService.addOpenTimes(dto);
            return ResponseEntity.ok("نوبت دهی با موفقیت انجام شد!");
    }
    @PostMapping("/{appointmentId}/take")
    public ResponseEntity<String> takeAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody TakeAppointmentRequestDto requestDto) {
        appointmentService.takeAppointment(appointmentId, requestDto);
        return ResponseEntity.ok("نوبت بیمار با موفقیت ثبت شد.");
    }

}
