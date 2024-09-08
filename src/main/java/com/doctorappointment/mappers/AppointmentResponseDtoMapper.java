package com.doctorappointment.mappers;

import com.doctorappointment.dtos.AppointmentResponseDto;
import com.doctorappointment.entites.Appointment;
import com.doctorappointment.entites.Patient;
import com.doctorappointment.enums.Status;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentResponseDtoMapper {
    public AppointmentResponseDto toDto(Appointment appointment) {
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setId(appointment.getId());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setDay(appointment.getDay());
        dto.setStatus(appointment.getStatus());

        if (appointment.getPatient() != null) {
            dto.setPatientName(appointment.getPatient().getName());
            dto.setPatientPhone(appointment.getPatient().getPhoneNumber());
        }

        return dto;
    }

    public List<AppointmentResponseDto> toDtoList(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
