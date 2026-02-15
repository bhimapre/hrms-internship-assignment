package com.example.hrms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingParticipantsDto {
    private UUID employeeId;
    private String employeeName;
    private String gameRole;
}
