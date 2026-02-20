package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.JobOpeningDto;
import com.example.hrms_backend.dto.JobOpeningViewDto;
import com.example.hrms_backend.services.JobOpeningService;
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
public class JobOpeningController {

    private final JobOpeningService jobOpeningService;

    @PreAuthorize("hasAuthority('HR')")
    @PostMapping(value = "/api/hr/job-opening", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JobOpeningDto> createJobOpening(@RequestPart("file") MultipartFile file, @Valid @RequestPart("data") String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JobOpeningDto openingDto = mapper.readValue(data, JobOpeningDto.class);
        JobOpeningDto jobOpening = jobOpeningService.createJobOpening(openingDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobOpening);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/job-opening")
    public ResponseEntity <Page<JobOpeningViewDto>> allOpenJobs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<JobOpeningViewDto> jobOpeningDto = jobOpeningService.getAllOpenJobs(pageable);
        return ResponseEntity.ok(jobOpeningDto);
    }

    @PreAuthorize("hasAuthority('HR')")
    @GetMapping("/api/hr/job-opening")
    public ResponseEntity<Page<JobOpeningViewDto>> allJobs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<JobOpeningViewDto> jobOpenings = jobOpeningService.getAllJobs(pageable);
        return ResponseEntity.ok(jobOpenings);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/job-opening/{jobOpeningId}")
    public ResponseEntity<JobOpeningViewDto> getOpenJobById(@PathVariable UUID jobOpeningId){
        JobOpeningViewDto jobOpening = jobOpeningService.getOpenJobById(jobOpeningId);
        return new ResponseEntity<>(jobOpening, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @GetMapping("/api/hr/job-opening/{jobOpeningId}")
    public ResponseEntity<JobOpeningViewDto> getJobById(@PathVariable UUID jobOpeningId){
        JobOpeningViewDto jobOpening = jobOpeningService.getJobById(jobOpeningId);
        return new ResponseEntity<>(jobOpening, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @PutMapping("/api/hr/job-opening/{jobOpeningId}")
    public ResponseEntity<JobOpeningDto> updateJobOpeningDetails(@PathVariable UUID jobOpeningId, @Valid @RequestBody JobOpeningDto jobOpeningDto){
        JobOpeningDto openingDto = jobOpeningService.updateJobOpening(jobOpeningId, jobOpeningDto);
        return new ResponseEntity<>(openingDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @PutMapping("/api/hr/job-opening/file/{jobOpeningId}")
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
