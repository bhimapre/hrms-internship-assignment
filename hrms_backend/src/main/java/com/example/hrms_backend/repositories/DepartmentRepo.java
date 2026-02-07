package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepo extends JpaRepository<Department, UUID> {
}
