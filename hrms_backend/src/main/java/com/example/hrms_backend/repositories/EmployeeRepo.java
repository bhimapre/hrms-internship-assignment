package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepo extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);

    List<Employee> findByManager_EmployeeId(UUID managerId);

    UUID user(User user);
}
