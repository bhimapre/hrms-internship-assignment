package com.example.hrms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class TravelDocumentDto {

    private UUID travelDocumentId;

    @NotBlank(message = "Document Name is required")
    @Size(min = 2, max =  100, message = "Document Name must be between 2 and 100 characters long")
    private String documentName;

    @NotBlank(message = "Owner Type is required")
    private String ownerType;

    @NotBlank(message = "file url is required")
    private String travelDocumentFileUrl;

    private UUID travelId;
    private UUID employeeID;
}
