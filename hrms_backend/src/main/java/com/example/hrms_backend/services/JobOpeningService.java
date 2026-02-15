package com.example.hrms_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.hrms_backend.dto.JobOpeningDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.JobOpening;
import com.example.hrms_backend.entities.enums.JobOpeningStatus;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.JobOpeningRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
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
public class JobOpeningService {

    private final JobOpeningRepo jobOpeningRepo;
    private final ModelMapper modelMapper;
    private final EmployeeRepo employeeRepo;
    private final Cloudinary cloudinary;
    private static final String JOB_NOT_FOUND = "Job not found";
    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";

    // Create job opening
    public JobOpeningDto createJobOpening(JobOpeningDto jobOpeningDto, MultipartFile file) throws IOException {

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

        JobOpening jobOpening = modelMapper.map(jobOpeningDto, JobOpening.class);

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "jobOpening/jobDescription")
        );

        jobOpening.setJobDescriptionFileUrl(uploadResult.get("secure_url").toString());
        jobOpening.setPublicId(uploadResult.get("public_id").toString());
        jobOpening.setJobOpeningStatus(JobOpeningStatus.OPEN);
        jobOpening.setCreatedBy(employeeId);
        jobOpening.setCreatedAt(LocalDateTime.now());

        jobOpening = jobOpeningRepo.save(jobOpening);
        return modelMapper.map(jobOpening, JobOpeningDto.class);
    }

    // Get all open job openings
    public List<JobOpeningDto> getAllOpenJobs(){
        List<JobOpening>  jobOpenings = jobOpeningRepo.findAllByJobOpeningStatus(JobOpeningStatus.OPEN);
        return jobOpenings.stream()
                .map(job -> modelMapper.map(job, JobOpeningDto.class))
                .toList();
    }

    // Get all job openings
    public List<JobOpeningDto> getAllJobs()
    {
        String role = SecurityUtils.getRole();
        if(!role.equals("HR")){
            throw new AccessDeniedException("You have no access of it");
        }

        List<JobOpening> jobs = jobOpeningRepo.findAll();
        return jobs.stream()
                .map(job -> modelMapper.map(job, JobOpeningDto.class))
                .toList();
    }

    // Get open job opening by id
    public JobOpeningDto getOpenJobById(UUID jobOpeningId){
        JobOpening jobOpening = jobOpeningRepo.findById(jobOpeningId).orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));

        if(!jobOpening.getJobOpeningStatus().equals(JobOpeningStatus.OPEN)){
            throw new BadRequestException("Job is not open");
        }

        return modelMapper.map(jobOpening, JobOpeningDto.class);
    }

    // Get all job opening by id
    public JobOpeningDto getJobById(UUID jobOpeningId){
        String role = SecurityUtils.getRole();
        if(!role.equals("HR")){
            throw new AccessDeniedException("You have no access of it");
        }

        JobOpening jobOpening = jobOpeningRepo.findById(jobOpeningId).orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));
        return modelMapper.map(jobOpening, JobOpeningDto.class);
    }

    // Update job opening
    public JobOpeningDto updateJobOpening(UUID jobOpeningId, JobOpeningDto jobOpeningDto){

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        JobOpening jobOpening = jobOpeningRepo.findById(jobOpeningId).orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));

        jobOpening.setJobTitle(jobOpeningDto.getJobTitle());
        jobOpening.setJobDescription(jobOpeningDto.getJobDescription());
        jobOpening.setJobLocation(jobOpeningDto.getJobLocation());
        jobOpening.setNoOfOpening(jobOpeningDto.getNoOfOpening());
        jobOpening.setExperience(jobOpeningDto.getExperience());
        jobOpening.setJobType(jobOpeningDto.getJobType());
        jobOpening.setJobOpeningStatus(jobOpeningDto.getJobOpeningStatus());
        jobOpening.setUpdatedAt(LocalDateTime.now());
        jobOpening.setUpdatedBy(employeeId);

        JobOpening updated = jobOpeningRepo.save(jobOpening);
        return modelMapper.map(updated, JobOpeningDto.class);
    }

    // Update job description file
    public JobOpeningDto updateJobDescriptionFile(UUID jobOpeningId, MultipartFile newFile) throws IOException{

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();
        JobOpening jobOpening = jobOpeningRepo.findById(jobOpeningId).orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));

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

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                newFile.getBytes(),
                ObjectUtils.asMap("folder", "jobOpening/jobDescription")
        );

        cloudinary.uploader().destroy(
                jobOpening.getPublicId(),
                ObjectUtils.emptyMap()
        );

        jobOpening.setJobDescriptionFileUrl(uploadResult.get("secure_url").toString());
        jobOpening.setPublicId(uploadResult.get("public_id").toString());
        jobOpening.setUpdatedBy(employeeId);
        jobOpening.setUpdatedAt(LocalDateTime.now());

        jobOpeningRepo.save(jobOpening);
        return modelMapper.map(jobOpening, JobOpeningDto.class);
    }

    // Soft delete change status to closed
    public JobOpeningDto closedJobOpeningUsingPatch(UUID jobOpeningId)
    {
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();
        JobOpening jobOpening = jobOpeningRepo.findById(jobOpeningId).orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));

        jobOpening.setJobOpeningStatus(JobOpeningStatus.CLOSED);
        jobOpening.setUpdatedBy(employeeId);
        jobOpening.setUpdatedAt(LocalDateTime.now());

        jobOpening = jobOpeningRepo.save(jobOpening);
        return modelMapper.map(jobOpening, JobOpeningDto.class);
    }
}
