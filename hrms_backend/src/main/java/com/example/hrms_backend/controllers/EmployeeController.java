package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.EmployeeDto;
import com.example.hrms_backend.dto.EmployeeProfileUpdate;
import com.example.hrms_backend.services.EmployeeService;
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
public class EmployeeController {

    private final EmployeeService employeeService;

    // Get all employees only accessible by HR
    @PreAuthorize("hasRole('HR')")
    @GetMapping("/api/hr/get-all-employee")
    public ResponseEntity<List<EmployeeDto>> getallEmployees(){
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // Get employee by id accessible by all
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/common/employee/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable UUID employeeId){
        EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employeeDto);
    }

    // Add employee by HR
    @PreAuthorize("hasRole('HR')")
    @PostMapping("/api/hr/add-employee")
    public ResponseEntity<EmployeeDto> addEmployee(@Valid @RequestBody EmployeeDto employeeDto){
        EmployeeDto employee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    // Updated employee details by HR
    @PreAuthorize("hasRole('HR')")
    @PutMapping("/api/hr/update-employee/{employeeId}")
    public ResponseEntity<EmployeeDto> updateEmployee(@Valid @PathVariable UUID employeeId, @RequestBody EmployeeDto employeeDto){
        EmployeeDto updateEmployee =employeeService.updateEmployee(employeeId, employeeDto);
        return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
    }

    // Update game preference by login user
    @PatchMapping("/api/common/employee/{employeeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> employeeProfileUpdate(@PathVariable UUID employeeId, @Valid @RequestBody EmployeeProfileUpdate profileUpdateDto){
        employeeService.updateSelfProfile(employeeId, profileUpdateDto);
        return ResponseEntity.noContent().build();
    }
}
