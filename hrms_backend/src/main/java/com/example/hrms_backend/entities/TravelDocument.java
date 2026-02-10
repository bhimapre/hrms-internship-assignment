package com.example.hrms_backend.entities;

import com.example.hrms_backend.entities.enums.DocumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "travel_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "travel_document_id")
    private UUID travelDocumentId;

    @Column(name = "document_name", nullable = false)
    @NotBlank(message = "Document Name is required")
    @Size(min = 2, max =  100, message = "Document Name must be between 2 and 100 characters long")
    private String documentName;

    @Column(name = "owner_type", nullable = false)
    @NotBlank(message = "Owner Type is required")
    private String ownerType;

    @Column(name = "travel_document_file_url", nullable = false)
    @NotNull(message = "file url is required")
    private String travelDocumentFileUrl;

    @Column(name =  "public_id", nullable = false)
    @NotNull(message = "public id is required")
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @Column(name = "uploaded_by", nullable = false)
    @NotNull(message = "Uploaded by required")
    private UUID uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
