package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.JobShareDto;
import com.example.hrms_backend.dto.JobShareRequestDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.JobOpening;
import com.example.hrms_backend.entities.JobShare;
import com.example.hrms_backend.entities.JobShareEmails;
import com.example.hrms_backend.entities.enums.EmailStatus;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.JobOpeningRepo;
import com.example.hrms_backend.repositories.JobShareEmailRepo;
import com.example.hrms_backend.repositories.JobShareRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobShareService {

    private final EmployeeRepo employeeRepo;
    private final JobShareRepo jobShareRepo;
    private final ModelMapper modelMapper;
    private final JobShareEmailRepo emailRepo;
    private final EmailService emailService;
    private final JobOpeningRepo jobOpeningRepo;

    // Job share
    public JobShareDto createJobShare(JobShareRequestDto requestDto){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        JobShare jobShare = modelMapper.map(requestDto, JobShare.class);

        if(requestDto.getJobShareEmailIds() == null || requestDto.getJobShareEmailIds().isEmpty()){
            throw new BadRequestException("At least one email id is required");
        }

        jobShare.setEmployee(employee);
        jobShare.setCreatedBy(employeeId);
        jobShare.setCreatedAt(LocalDateTime.now());

        jobShare = jobShareRepo.save(jobShare);

        JobOpening jobOpening = jobOpeningRepo.findById(requestDto.getJobOpening()).orElseThrow(() -> new ResourceNotFoundException("Job opening not found"));

        List<JobShareEmails> emailsList = new ArrayList<>();
        for (String email : requestDto.getJobShareEmailIds()){
            JobShareEmails jobShareEmails = new JobShareEmails();

            jobShareEmails.setEmail(email);
            jobShareEmails.setJobShare(jobShare);
            jobShareEmails.setCreatedAt(LocalDateTime.now());
            jobShareEmails.setCreatedBy(employeeId);

            try{
                emailService.sendJobShareEmail(email, jobOpening.getJobTitle(), jobOpening.getJobLocation(), jobOpening.getExperience(),
                        employee.getName(),jobOpening.getJobDescription(), jobOpening.getJobDescriptionFileUrl());

                jobShareEmails.setEmailStatus(EmailStatus.SENT);
            }
            catch (Exception e) {
                jobShareEmails.setEmailStatus(EmailStatus.FAILED);
            }

            emailsList.add(jobShareEmails);
            jobShareEmails = emailRepo.save(jobShareEmails);
        }
        return modelMapper.map(jobShare, JobShareDto.class);
    }
}
