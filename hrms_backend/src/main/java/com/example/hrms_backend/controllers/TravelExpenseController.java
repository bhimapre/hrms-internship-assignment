package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.TravelExpenseDto;
import com.example.hrms_backend.services.TravelExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TravelExpenseController {

    private final TravelExpenseService travelExpenseService;

    @PostMapping("/api/expense")
    public ResponseEntity<TravelExpenseDto> addTravelExpense(@Valid @RequestBody TravelExpenseDto travelExpenseDto){
        TravelExpenseDto expense = travelExpenseService.addExpense(travelExpenseDto);
        return new ResponseEntity<>(expense, HttpStatus.CREATED);
    }

    @PostMapping("api/expense/submit/{expenseId}")
    public ResponseEntity<Void> submitTravelExpense(@PathVariable UUID expenseId){
        travelExpenseService.submitExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('HR')")
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

    @GetMapping("/api/expense")
    public ResponseEntity<List<TravelExpenseDto>> getAllExpense(){
        return ResponseEntity.ok(travelExpenseService.getAllExpense());
    }

    @GetMapping("/api/expense/{expenseId}")
    public ResponseEntity<TravelExpenseDto> getExpenseById(@PathVariable UUID expenseId){
        return ResponseEntity.ok(travelExpenseService.getTravelExpenseById(expenseId));
    }

    @PutMapping("/api/expense/{expenseId}")
    public ResponseEntity<Void> cancelExpense(@PathVariable UUID expenseId){
        travelExpenseService.cancelExpense(expenseId);
        return ResponseEntity.noContent().build();
    }
}
