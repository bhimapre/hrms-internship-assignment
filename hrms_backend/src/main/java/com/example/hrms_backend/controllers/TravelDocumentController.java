package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.TravelDocumentDto;
import com.example.hrms_backend.entities.TravelDocument;
import com.example.hrms_backend.services.TravelDocumentService;
import com.example.hrms_backend.utils.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @PostMapping(value = "/{travelId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadTravelDocument(@PathVariable UUID travelId, @RequestPart("file")MultipartFile file, @Valid @RequestPart("data")String data) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        TravelDocumentDto documentDto = mapper.readValue(data, TravelDocumentDto.class);
        String fileName = travelDocumentService.uploadTravelDocuments(travelId, file, documentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileName);
    }

    // Get all documents
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<TravelDocumentDto>> getAllDocuments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<TravelDocumentDto> documents = travelDocumentService.getAllTravelDocuments(pageable);
        return ResponseEntity.ok(documents);
    }

    // Get document by id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{documentId}")
    public ResponseEntity<TravelDocumentDto> getDocumentById(@PathVariable UUID documentId){
        return ResponseEntity.ok(travelDocumentService.getTravelDocumentsById(documentId));
    }

    // Update Document details
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{documentId}")
    public ResponseEntity<TravelDocumentDto> updateDocument(@PathVariable UUID documentId, @RequestBody TravelDocumentDto documentDto){
        TravelDocumentDto updated = travelDocumentService.updateTravelDocumentDetails(documentId, documentDto);
        return ResponseEntity.ok(updated);
    }

    // Update document file
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/file/{documentId}")
    public ResponseEntity<TravelDocumentDto> updateDocumentFile(@PathVariable UUID documentId, @RequestPart("file") MultipartFile file) throws IOException{
        TravelDocumentDto updated = travelDocumentService.updateTravelDocumentFile(documentId, file);
        return ResponseEntity.ok(updated);
    }

    // Fetch document details using travel ID
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{travelId}")
    public ResponseEntity<Page<TravelDocumentDto>> getTravelDocumentsByTravelId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size, @PathVariable UUID travelId){
        Pageable pageable = PageRequest.of(page, size);
        Page<TravelDocumentDto> documents = travelDocumentService.getDocumentsByTravelId(pageable, travelId);
        return ResponseEntity.ok(documents);
    }

    // Fetch All documents based on travel ID and employee ID
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{travelId}/{employeeId}")
    public ResponseEntity<List<TravelDocumentDto>> getDocumentsByTravelIdAndEmployeeId(@PathVariable UUID travelId, @PathVariable UUID employeeId){
        List<TravelDocumentDto> documents = travelDocumentService.getDocumentsByTravelIdAndExpenseId(travelId, employeeId);
        return ResponseEntity.ok(documents);
    }

    // Get All Document based on travel ID and uploadedby HR
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/document-hr/{travelId}")
    public ResponseEntity<List<TravelDocumentDto>> getDocumentByHRAndTravelId(@PathVariable UUID travelId){
        List<TravelDocumentDto> documents = travelDocumentService.getDocumentsByTravelIdAndHR(travelId);
        return ResponseEntity.ok(documents);
    }
}
