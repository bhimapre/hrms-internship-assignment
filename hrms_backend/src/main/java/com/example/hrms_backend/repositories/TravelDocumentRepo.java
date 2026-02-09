package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.TravelDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TravelDocumentRepo extends JpaRepository<TravelDocument, UUID> {

    List<TravelDocument> findByUploadedBy(UUID uploadedBy);
}
