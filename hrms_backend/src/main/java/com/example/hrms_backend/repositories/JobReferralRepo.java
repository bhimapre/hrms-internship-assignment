package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.JobReferral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobReferralRepo extends JpaRepository<JobReferral, UUID> {

    Page<JobReferral> findByCreatedBy(UUID createdBy, Pageable pageable);
}
