package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.JobReferralDto;
import com.example.hrms_backend.dto.JobReferralStatusDto;
import com.example.hrms_backend.services.JobReferralService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JobReferralController {

    private JobReferralService jobReferralService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/job-referral/{jobOpeningId}")
    public ResponseEntity<JobReferralDto> createJobReferral(@PathVariable UUID jobOpeningId, @Valid @RequestPart("data") JobReferralDto referralDto, @RequestPart("file") MultipartFile file) throws IOException {
        JobReferralDto referral = jobReferralService.createJobReferral(jobOpeningId, referralDto, file);
        log.info("Job referral created: id={}, name={}, email={}", referralDto.getJobReferralId(), referralDto.getName(), referralDto.getEmail());
        return new ResponseEntity<>(referral, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/job-referral")
    public ResponseEntity<List<JobReferralDto>> getAllReferralByUser(){
        List<JobReferralDto> referrals = jobReferralService.getAllJobReferralsBasedOnUser();
        return new ResponseEntity<>(referrals, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @GetMapping("/api/hr/job-referral")
    public ResponseEntity<List<JobReferralDto>> getAllReferralByHR(){
        List<JobReferralDto> referrals = jobReferralService.getAllJobReferralsForHR();
        return new ResponseEntity<>(referrals, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/job-referral/{jobReferralId}")
    public ResponseEntity<JobReferralDto> getReferralById(@PathVariable UUID jobReferralId){
        JobReferralDto referral = jobReferralService.getJobReferralById(jobReferralId);
        return new ResponseEntity<>(referral, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @PutMapping("/api/hr/job-referral-status/{jobReferralId}")
    public ResponseEntity<JobReferralDto> updateJobReferralStatus(@PathVariable UUID jobReferralId, @Valid @RequestBody JobReferralStatusDto statusDto){
        JobReferralDto referralDto = jobReferralService.updateJobReferralStatus(jobReferralId, statusDto);
        log.info("Job referral status change: id={}, newStatus={}, updatedBy={}", jobReferralId, statusDto.getJobReferralStatus(), LocalDateTime.now());
        return new ResponseEntity<>(referralDto, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/api/job-referral/{jobReferralId}")
    public ResponseEntity<JobReferralDto> updateJobReferralDetails(@PathVariable UUID jobReferralId, @Valid @RequestBody JobReferralDto referralDto){
        JobReferralDto jobReferralDto = jobReferralService.updateJobReferral(jobReferralId, referralDto);
        return new ResponseEntity<>(jobReferralDto, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/api/job-referral-cv/{jobReferralId}")
    public ResponseEntity<JobReferralDto> updateJobReferralCv(@PathVariable UUID jobReferralId, @RequestPart("file")MultipartFile file) throws IOException{
        JobReferralDto jobReferralDto = jobReferralService.updateCvFile(jobReferralId, file);
        return new ResponseEntity<>(jobReferralDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HR')")
    @PatchMapping("/api/job-referral/{jobReferralId}")
    public ResponseEntity<JobReferralDto> deleteJobReferral(@PathVariable UUID jobReferralId){
        JobReferralDto jobReferralDto = jobReferralService.holdJobReferralUsingPatch(jobReferralId);
        return new ResponseEntity<>(jobReferralDto, HttpStatus.OK);
    }
}
