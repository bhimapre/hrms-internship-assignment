package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.JobShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobShareRepo extends JpaRepository<JobShare, UUID> {
}
