package com.example.hrms_backend.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class JobShareDto {
    private UUID jobShareId;
    private UUID jobOpening;
    private UUID employee;
}
