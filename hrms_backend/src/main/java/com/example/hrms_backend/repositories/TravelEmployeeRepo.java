package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.TravelEmployee;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface TravelEmployeeRepo extends JpaRepository<TravelEmployee, UUID> {

    boolean existsByTravel_TravelIdAndEmployee_EmployeeId(UUID travelId, UUID employeeId);

    boolean existsByTravel_TravelIdAndEmployee_EmployeeIdIn(UUID travelId, Set<UUID> employeeIds);

    Page<TravelEmployee> findByEmployee_EmployeeId(UUID employeeId, Pageable pageable);

    Page<TravelEmployee> findByEmployee_EmployeeIdIn(Collection<UUID> employeeIds, Pageable pageable);

    List<TravelEmployee> findByTravel_TravelId(UUID travelId);
}
