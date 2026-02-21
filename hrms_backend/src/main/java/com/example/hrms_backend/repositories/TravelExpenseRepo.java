package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.TravelExpense;
import com.example.hrms_backend.entities.enums.ExpenseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface TravelExpenseRepo extends JpaRepository<TravelExpense, UUID> {

    List<TravelExpense> findByEmployee_EmployeeId(UUID employeeId);

    List<TravelExpense> findByEmployee_EmployeeIdIn(Set<UUID> employeeIds);

    List<TravelExpense> findByTravel_TravelId(UUID travelTravelId);

    List<TravelExpense> findByTravel_TravelIdAndEmployee_EmployeeId(UUID travelTravelId, UUID employeeEmployeeId);

    @Query("""
    SELECT COALESCE(SUM(e.expenseAmount),0)
    FROM TravelExpense e 
    WHERE e.travel.travelId = :travelId
    AND e.expenseStatus = 'APPROVED'
""")
    BigDecimal getTotalExpenseApprovedAmount(@Param("travelId") UUID travelID);


    @Query("""
    SELECT te FROM TravelExpense te
    JOIN te.employee e
    JOIN te.travel t
    WHERE (:travelId IS NULL OR t.travelId =:travelId)
    AND (:employeeName IS  NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :employeeName, '%' )))
    AND (:travelTitle IS NULL OR LOWER(t.travelTitle) LIKE (CONCAT('%', :travelTitle, '%')))
    AND (:status IS NULL OR te.expenseStatus = :status)
    AND (:fromDate IS NULL OR te.expenseDate >= :fromDate)
    AND (:toDate IS NULL OR te.expenseDate <= :toDate)
    
""")
    Page<TravelExpense> searchHrExpense(@Param("travelId") UUID travelId,
                                        @Param("employeeName") String employeeName,
                                        @Param("travelTitle") String travelTitle,
                                        @Param("status")ExpenseStatus status,
                                        @Param("fromDate")LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate,
                                        Pageable pageable);
}


