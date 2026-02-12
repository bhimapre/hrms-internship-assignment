package com.example.hrms_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.hrms_backend.dto.JobOpeningDto;
import com.example.hrms_backend.dto.JobReferralDto;
import com.example.hrms_backend.dto.JobReferralStatusDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.JobOpening;
import com.example.hrms_backend.entities.JobReferral;
import com.example.hrms_backend.entities.enums.EmailStatus;
import com.example.hrms_backend.entities.enums.JobOpeningStatus;
import com.example.hrms_backend.entities.enums.JobReferralStatus;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.JobOpeningRepo;
import com.example.hrms_backend.repositories.JobReferralRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobReferralService {

    private final JobReferralRepo jobReferralRepo;
    private final EmployeeRepo employeeRepo;
    private final JobOpeningRepo jobOpeningRepo;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;
    private static final String JOB_REFERRAL_NOT_FOUND = "Job referral not found";
    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";
    private final EmailService emailService;

    // Create job referral
    public JobReferralDto createJobReferral(UUID jobOpeningId, JobReferralDto jobReferralDto, MultipartFile file) throws IOException{

        String contentType = file.getContentType();
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        if (contentType == null || !(contentType.equals("application/pdf") || contentType.equals("application/msword"))) {
            throw new BadRequestException("Only PDF, MS WORD are allowed");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("File size must be less than 10 MB");
        }

        JobOpening jobOpening = jobOpeningRepo.findById(jobOpeningId).orElseThrow(() -> new BadRequestException("Job Opening Not Found"));

        JobReferral jobReferral = modelMapper.map(jobReferralDto, JobReferral.class);

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "jobOpening/jobReferrals")
        );


        jobReferral.setCvFileUrl(uploadResult.get("secure_url").toString());
        jobReferral.setPublicId(uploadResult.get("public_id").toString());
        jobReferral.setJobReferralStatus(JobReferralStatus.NEW);
        jobReferral.setJobOpening(jobOpening);
        jobReferral.setEmployee(employee);

        try{
            emailService.sendReferralEmail(jobReferral.getEmail(), jobReferral.getName(), jobOpening.getJobTitle(), employee.getName());
            jobReferral.setEmailStatus(EmailStatus.SENT);
        }
        catch (Exception e){
            jobReferral.setEmailStatus(EmailStatus.FAILED);
        }
        jobReferral.setCreatedAt(LocalDateTime.now());
        jobReferral.setCreatedBy(employeeId);

        jobReferralRepo.save(jobReferral);
        return modelMapper.map(jobReferral, JobReferralDto.class);
    }

    // Get all job referrals
    public List<JobReferralDto> getAllJobReferralsBasedOnUser(){

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        List<JobReferral> jobReferrals = jobReferralRepo.findByCreatedBy(employeeId);
        return jobReferrals.stream()
                .map(ref ->modelMapper.map(ref, JobReferralDto.class))
                .toList();
    }

    // Get all job referrals
    public List<JobReferralDto> getAllJobReferralsForHR(){

        List<JobReferral> jobReferrals = jobReferralRepo.findAll();
        return jobReferrals.stream()
                .map(ref ->modelMapper.map(ref, JobReferralDto.class))
                .toList();
    }

    // Get job referrals by id
    public JobReferralDto getJobReferralById(UUID jobReferralId){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();
        String role = SecurityUtils.getRole();

        JobReferral jobReferral = jobReferralRepo.findById(jobReferralId).orElseThrow(() -> new BadRequestException(JOB_REFERRAL_NOT_FOUND));
        if(!role.equals("HR")){
            if(!jobReferral.getCreatedBy().equals(employeeId)){
                throw new AccessDeniedException("You have no access of this job referral");
            }
            return modelMapper.map(jobReferral, JobReferralDto.class);
        }
        return modelMapper.map(jobReferral, JobReferralDto.class);
    }

    // Update job referral status
    public JobReferralDto updateJobReferralStatus(UUID jobReferralId, JobReferralStatusDto statusDto){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        JobReferral jobReferral = jobReferralRepo.findById(jobReferralId).orElseThrow(() -> new BadRequestException(JOB_REFERRAL_NOT_FOUND));
        jobReferral.setJobReferralStatus(statusDto.getJobReferralStatus());
        jobReferral.setStatusChangeTime(LocalDateTime.now());
        jobReferral.setUpdatedBy(employeeId);
        jobReferral.setUpdatedAt(LocalDateTime.now());

        jobReferralRepo.save(jobReferral);
        return modelMapper.map(jobReferral, JobReferralDto.class);
    }

    // update job referral
    public JobReferralDto updateJobReferral(UUID jobReferralId, JobReferralDto jobReferralDto){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();
        String role = SecurityUtils.getRole();

        JobReferral jobReferral = jobReferralRepo.findById(jobReferralId).orElseThrow(() -> new ResourceNotFoundException(JOB_REFERRAL_NOT_FOUND));

        if(!role.equals("HR")){
            if(!jobReferral.getEmployee().getEmployeeId().equals(employeeId)){
                throw new BadRequestException("You have no access of this job referral");
            }
        }

        if (!jobReferral.getJobReferralStatus().equals(JobReferralStatus.NEW)) {
            throw new BadRequestException("You cannot update the referral");
        }

        jobReferral.setName(jobReferralDto.getName());
        jobReferral.setEmail(jobReferralDto.getEmail());
        jobReferral.setPhoneNumber(jobReferralDto.getPhoneNumber());
        jobReferral.setShortNote(jobReferralDto.getShortNote());
        jobReferral.setUpdatedBy(employeeId);
        jobReferral.setUpdatedAt(LocalDateTime.now());

        jobReferralRepo.save(jobReferral);
        return modelMapper.map(jobReferral, JobReferralDto.class);
    }

    // Update CV file
    public JobReferralDto updateCvFile(UUID jobReferralId, MultipartFile newFile) throws IOException {
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();
        String role = SecurityUtils.getRole();
        JobReferral jobReferral = jobReferralRepo.findById(jobReferralId).orElseThrow(() -> new ResourceNotFoundException(JOB_REFERRAL_NOT_FOUND));

        if (!jobReferral.getJobReferralStatus().equals(JobReferralStatus.NEW)) {
            throw new BadRequestException("You cannot update the referral");
        }

        if(!role.equals("HR")){
            if(!jobReferral.getEmployee().getEmployeeId().equals(employeeId)){
                throw new BadRequestException("You have no access of this job referral");
            }
        }

        if (newFile == null || newFile.isEmpty()) {
            throw new BadRequestException("File is required");
        }

        String contentType = newFile.getContentType();
        if (contentType == null || !(contentType.equals("application/pdf") || contentType.equals("application/msword"))) {
            throw new BadRequestException("Only PDF, MS WORD are allowed");
        }

        if (newFile.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("File size must be less than 10MB");
        }

        cloudinary.uploader().destroy(
                jobReferral.getPublicId(),
                ObjectUtils.emptyMap()
        );

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                newFile.getBytes(),
                ObjectUtils.asMap("folder", "jobOpening/jobReferrals")
        );

        jobReferral.setCvFileUrl(uploadResult.get("secure_url").toString());
        jobReferral.setPublicId(uploadResult.get("public_id").toString());
        jobReferral.setUpdatedAt(LocalDateTime.now());
        jobReferral.setUpdatedBy(employeeId);

        jobReferralRepo.save(jobReferral);
        return modelMapper.map(jobReferral, JobReferralDto.class);
    }

    // Soft delete change job referral status to Hold
    public JobReferralDto holdJobReferralUsingPatch(UUID jobReferralId)
    {
        String role = SecurityUtils.getRole();
        if(!role.equals("HR")){
            throw new AccessDeniedException("You have no access of it");
        }
        JobReferral jobReferral = jobReferralRepo.findById(jobReferralId).orElseThrow(() -> new RuntimeException(JOB_REFERRAL_NOT_FOUND));

        jobReferral.setJobReferralStatus(JobReferralStatus.HOLD);
        jobReferralRepo.save(jobReferral);
        return modelMapper.map(jobReferral, JobReferralDto.class);
    }
}
