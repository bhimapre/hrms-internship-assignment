package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.EmployeeDto;
import com.example.hrms_backend.dto.EmployeeProfileUpdate;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.DepartmentRepo;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.GameRepo;
import com.example.hrms_backend.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final ModelMapper modelMapper;
    private final DepartmentRepo departmentRepo;
    private final UserRepo userRepo;
    private final GameRepo gameRepo;
    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";

    // Get All Employees
    public List<EmployeeDto> getAllEmployees(){
        List<Employee> employees = employeeRepo.findAll();
        return employees.stream()
                .map(emp -> modelMapper.map(emp, EmployeeDto.class))
                .toList();
    }

    // Get Employee By ID
    public EmployeeDto getEmployeeById(UUID employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new ResolutionException(EMPLOYEE_NOT_FOUND));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    // Add Employee
    public EmployeeDto createEmployee(EmployeeDto employeeDto){
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        employee.setName(employeeDto.getName());
        employee.setDob(employeeDto.getDob());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setAddress(employeeDto.getAddress());
        employee.setJoiningDate(employeeDto.getJoiningDate());
        employee.setDesignation(employeeDto.getDesignation());
        employee.setDepartment(departmentRepo.findById(employeeDto.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found")));
        if(employeeDto.getManagerId() != null)
        {
            employeeRepo.findById(employeeDto.getManagerId()).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        }
        employee.setUser(userRepo.findById(employeeDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found")));

        Employee saved = employeeRepo.save(employee);
        return modelMapper.map(saved, EmployeeDto.class);
    }

    // Update Employee
    public EmployeeDto updateEmployee(UUID employeeId, EmployeeDto employeeDto){
        Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));

        employee.setName(employeeDto.getName());
        employee.setDob(employeeDto.getDob());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setAddress(employeeDto.getAddress());
        employee.setJoiningDate(employeeDto.getJoiningDate());
        employee.setDesignation(employeeDto.getDesignation());
        employee.setDepartment(departmentRepo.findById(employeeDto.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found")));
        if(employeeDto.getManagerId() != null)
        {
            employeeRepo.findById(employeeDto.getManagerId()).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        }
        employee.setUser(userRepo.findById(employeeDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found")));
        Employee updateEmployee = employeeRepo.save(employee);
        return modelMapper.map(updateEmployee, EmployeeDto.class);
    }

    // Update game preference by employee
    public void updateSelfProfile(UUID employeeId, EmployeeProfileUpdate profileDto){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginEmail = auth.getName();

        Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));

        if(!employee.getEmail().equals(loginEmail))
        {
            throw new AccessDeniedException("You cannot change other's game preference");
        }

        if(profileDto.getGamePreferences() != null){
            employee.setGamePreferences(
                    new HashSet<>(gameRepo.findAllById(profileDto.getGamePreferences()))
            );
        }

        employeeRepo.save(employee);
    }

}
