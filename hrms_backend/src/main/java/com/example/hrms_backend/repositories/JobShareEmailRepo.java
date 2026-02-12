package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.JobShareEmails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobShareEmailRepo extends JpaRepository<JobShareEmails, UUID> {
}
