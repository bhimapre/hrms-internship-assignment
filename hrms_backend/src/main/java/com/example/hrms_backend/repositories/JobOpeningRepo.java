package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.JobOpening;
import com.example.hrms_backend.entities.enums.JobOpeningStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobOpeningRepo extends JpaRepository<JobOpening, UUID> {

    Page<JobOpening> findAllByJobOpeningStatus(JobOpeningStatus status, Pageable pageable);
}
