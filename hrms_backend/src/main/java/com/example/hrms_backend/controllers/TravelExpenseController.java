package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.TravelExpenseDto;
import com.example.hrms_backend.entities.enums.ExpenseStatus;
import com.example.hrms_backend.services.TravelExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TravelExpenseController {

    private final TravelExpenseService travelExpenseService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/expense/{travelId}")
    public ResponseEntity<TravelExpenseDto> addTravelExpense(@PathVariable UUID travelId,@Valid @RequestBody TravelExpenseDto travelExpenseDto){
        TravelExpenseDto expense = travelExpenseService.addExpense(travelId ,travelExpenseDto);
        return new ResponseEntity<>(expense, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("api/expense/submit/{expenseId}")
    public ResponseEntity<Void> submitTravelExpense(@PathVariable UUID expenseId){
        travelExpenseService.submitExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('HR')")
    @PutMapping("api/hr/expense/approve/{expenseId}")
    public ResponseEntity<Void> approveExpense(@PathVariable UUID expenseId, @RequestParam String remark){
        travelExpenseService.approveExpense(expenseId, remark);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('HR')")
    @PutMapping("api/hr/expense/reject/{expenseId}")
    public ResponseEntity<Void> rejectExpense(@PathVariable UUID expenseId, @RequestParam String remark){
        travelExpenseService.rejectExpense(expenseId, remark);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/expense")
    public ResponseEntity<List<TravelExpenseDto>> getAllExpense(){
        return ResponseEntity.ok(travelExpenseService.getAllExpense());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/expense/{expenseId}")
    public ResponseEntity<TravelExpenseDto> getExpenseById(@PathVariable UUID expenseId){
        return ResponseEntity.ok(travelExpenseService.getTravelExpenseById(expenseId));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/api/expense/cancel/{expenseId}")
    public ResponseEntity<Void> cancelExpense(@PathVariable UUID expenseId){
        travelExpenseService.cancelExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/api/expense/{expenseId}")
    public ResponseEntity<TravelExpenseDto> updateExpenseDetails(@PathVariable UUID expenseId, @Valid @RequestBody TravelExpenseDto travelExpenseDto){
        travelExpenseService.updateTravelExpenseDetails(expenseId, travelExpenseDto);
        return new ResponseEntity<>(travelExpenseDto, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("api/hr/expense/filter")
    public ResponseEntity<Page<TravelExpenseDto>> getExpenseFilter(@RequestParam UUID travelId,
                                                                   @RequestParam(required = false) String employeeName,
                                                                   @RequestParam(required = false) String travelTitle,
                                                                   @RequestParam(required = false)ExpenseStatus status,
                                                                   @RequestParam(required = false)LocalDate fromDate,
                                                                   @RequestParam(required = false) LocalDate toDate,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size)
    {
        Page<TravelExpenseDto> expenseDto = travelExpenseService.getHrExpense(travelId, employeeName, travelTitle, status, fromDate, toDate ,PageRequest.of(page, size,
                Sort.by("createdAt").descending()));

        return ResponseEntity.ok(expenseDto);
    }

    // Get travel expense based on travel ID
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/hr/travel-expense/{travelId}")
    public ResponseEntity<List<TravelExpenseDto>> getExpenseByTravelId(@PathVariable UUID travelId){
        return ResponseEntity.ok(travelExpenseService.getTravelExpenseByTravelId(travelId));
    }

    // Get Expenses based on travel id and employee id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/travel-expense/{travelId}/{employeeId}")
    public ResponseEntity<List<TravelExpenseDto>> getExpenseByTravelIdAndEmployeeId(@PathVariable UUID travelId, @PathVariable UUID employeeId){
        return ResponseEntity.ok(travelExpenseService.getAllExpenseByTravelIdAndEmployeeId(travelId, employeeId));
    }
}
