package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepo extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);

    List<Employee> findByManager_EmployeeId(UUID managerId);

    Optional<Employee> findByUser_UserId(UUID userId);

    List<Employee> findByIsActive(boolean isActive);

    @Query("""
    SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(e.email) LIKE LOWER(CONCAT('%', :query, '%'))
""")
    List<Employee> serchEmployees(@Param("query") String query);
}
