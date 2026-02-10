package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.TravelExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface TravelExpenseRepo extends JpaRepository<TravelExpense, UUID> {

    List<TravelExpense> findByEmployee_EmployeeId(UUID employeeId);

    List<TravelExpense> findByEmployee_EmployeeIdIn(Set<UUID> employeeIds);
}
