package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.JobOpeningDto;
import com.example.hrms_backend.dto.TravelDocumentDto;
import com.example.hrms_backend.entities.JobOpening;
import com.example.hrms_backend.services.JobOpeningService;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class JobOpeningController {

    private JobOpeningService jobOpeningService;

    @PreAuthorize("hasAuthority('HR')")
    @PostMapping("/api/hr/job-opening")
    public ResponseEntity<JobOpeningDto> createJobOpening(@RequestPart("file") MultipartFile file, @Valid @RequestPart("data") JobOpeningDto jobOpeningDto) throws IOException {
        JobOpeningDto jobOpening = jobOpeningService.createJobOpening(jobOpeningDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobOpening);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/open/job-opening")
    public ResponseEntity <List<JobOpeningDto>> allOpenJobs(){
       List<JobOpeningDto> jobOpeningDto = jobOpeningService.getAllOpenJobs();
       return new ResponseEntity<>(jobOpeningDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @GetMapping("/api/hr/job-opening")
    public ResponseEntity<List<JobOpeningDto>> allJobs(){
        List<JobOpeningDto> jobOpenings = jobOpeningService.getAllJobs();
        return new ResponseEntity<>(jobOpenings, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/open/job-opening/{jobOpeningId}")
    public ResponseEntity<JobOpeningDto> getOpenJobById(@PathVariable UUID jobOpeningId){
        JobOpeningDto jobOpening = jobOpeningService.getOpenJobById(jobOpeningId);
        return new ResponseEntity<>(jobOpening, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @GetMapping("/api/hr/job-opening/{jobOpeningId}")
    public ResponseEntity<JobOpeningDto> getJobById(@PathVariable UUID jobOpeningId){
        JobOpeningDto jobOpening = jobOpeningService.getJobById(jobOpeningId);
        return new ResponseEntity<>(jobOpening, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @PutMapping("/api/hr/job-opening/{jobOpeningId}")
    public ResponseEntity<JobOpeningDto> updateJobOpeningDetails(@PathVariable UUID jobOpeningId, @Valid @RequestBody JobOpeningDto jobOpeningDto){
        JobOpeningDto openingDto = jobOpeningService.updateJobOpening(jobOpeningId, jobOpeningDto);
        return new ResponseEntity<>(openingDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @PutMapping("/api/hr/job-opening/{jobOpeningId}")
    public ResponseEntity<JobOpeningDto> updateJobDescription(@PathVariable UUID jobOpeningId, @RequestPart("file")MultipartFile file) throws IOException{
        JobOpeningDto openingDto = jobOpeningService.updateJobDescriptionFile(jobOpeningId, file);
        return new ResponseEntity<>(openingDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @PatchMapping("api/hr/job-opening/{jobOpeningId}")
    public ResponseEntity<JobOpeningDto> closeJobOpening(@PathVariable UUID jobOpeningId){
        JobOpeningDto openingDto = jobOpeningService.closedJobOpeningUsingPatch(jobOpeningId);
        return new ResponseEntity<>(openingDto, HttpStatus.OK);
    }
}
