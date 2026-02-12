package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.JobOpening;
import com.example.hrms_backend.entities.enums.JobOpeningStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobOpeningRepo extends JpaRepository<JobOpening, UUID> {

    List<JobOpening> findAllByJobOpeningStatus(JobOpeningStatus status);
}
