package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.TravelDocumentDto;
import com.example.hrms_backend.services.TravelDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel-documents")
public class TravelDocumentController {

    private final TravelDocumentService travelDocumentService;

    // Add documents
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{travelId}")
    public ResponseEntity<String> uploadTravelDocument(@PathVariable UUID travelId, @RequestPart("file")MultipartFile file, @Valid @RequestPart("data")TravelDocumentDto documentDto) throws IOException{
        String fileName = travelDocumentService.uploadTravelDocuments(travelId, file, documentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileName);
    }

    // Get all documents
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<TravelDocumentDto>> getAllDocuments(){
        return ResponseEntity.ok(travelDocumentService.getAllTravelDocuments());
    }

    // Get document by id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{documentId}")
    public ResponseEntity<TravelDocumentDto> getDocumentById(@PathVariable UUID documentId){
        return ResponseEntity.ok(travelDocumentService.getTravelDocumentsById(documentId));
    }
    // Update Document
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{documentId}")
    public ResponseEntity<TravelDocumentDto> updateDocument(@PathVariable UUID documentId, @RequestBody TravelDocumentDto documentDto){
        TravelDocumentDto updated = travelDocumentService.updateTravelDocumentDetails(documentId, documentDto);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/file/{documentId}")
    public ResponseEntity<TravelDocumentDto> updateDocumentFile(@PathVariable UUID documentId, @RequestPart MultipartFile file) throws IOException{
        TravelDocumentDto updated = travelDocumentService.updateTravelDocumentFile(documentId, file);
        return ResponseEntity.ok(updated);
    }
}
