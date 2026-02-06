package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepo extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);
}
