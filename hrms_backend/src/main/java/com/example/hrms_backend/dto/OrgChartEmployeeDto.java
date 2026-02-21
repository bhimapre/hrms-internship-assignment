package com.example.hrms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgChartEmployeeDto {
    private UUID employeeId;
    private String name;
    private String designation;
    private String profilePictureFileUrl;
}
