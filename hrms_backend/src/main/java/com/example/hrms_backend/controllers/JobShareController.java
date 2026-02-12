package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.JobShareDto;
import com.example.hrms_backend.dto.JobShareRequestDto;
import com.example.hrms_backend.services.JobShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobShareController {

    private final JobShareService jobShareService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/job-share")
    public ResponseEntity<JobShareDto> createJobShare(@Valid @RequestBody JobShareRequestDto requestDto){
        JobShareDto jobShareDto = jobShareService.createJobShare(requestDto);
        return new ResponseEntity<>(jobShareDto, HttpStatus.CREATED);
    }
}
