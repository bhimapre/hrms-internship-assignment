package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.EmployeeDto;
import com.example.hrms_backend.dto.EmployeeProfileUpdate;
import com.example.hrms_backend.entities.Department;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Game;
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
import java.util.Set;
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
        employee.setEmail(employeeDto.getEmail());
        employee.setCity(employeeDto.getCity());
        employee.setDob(employeeDto.getDob());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setAddress(employeeDto.getAddress());
        employee.setJoiningDate(employeeDto.getJoiningDate());
        employee.setDesignation(employeeDto.getDesignation());
        Department department = departmentRepo.findById(employeeDto.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        employee.setDepartment(department);
        if(employeeDto.getManagerId() != null)
        {
            Employee emp = employeeRepo.findById(employeeDto.getManagerId()).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
            employee.setManager(emp);
        }
        employee.setUser(userRepo.findById(employeeDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found")));

        Employee saved = employeeRepo.save(employee);
        return modelMapper.map(saved, EmployeeDto.class);
    }

    // Update Employee
    public EmployeeDto updateEmployee(UUID employeeId, EmployeeDto employeeDto){
        Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));

        if(employeeDto.getName() != null){
            employee.setName(employeeDto.getName());
        }

        if(employeeDto.getEmail() != null){
            employee.setEmail(employeeDto.getEmail());
        }

        if(employeeDto.getCity() != null){
            employee.setCity(employeeDto.getCity());
        }

        if(employeeDto.getDob() != null){
            employee.setDob(employeeDto.getDob());
        }

        if(employeeDto.getPhoneNumber() != null){
            employee.setPhoneNumber(employeeDto.getPhoneNumber());
        }

        if(employeeDto.getAddress() != null){
            employee.setAddress(employeeDto.getAddress());
        }

        if(employeeDto.getJoiningDate() != null){
            employee.setJoiningDate(employeeDto.getJoiningDate());
        }

        if(employeeDto.getDesignation() != null){
            employee.setDesignation(employeeDto.getDesignation());
        }

        if(employeeDto.getDepartmentId() != null){
            Department department = departmentRepo.findById(employeeDto.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            employee.setDepartment(department);
        }

        if(employeeDto.getManagerId() != null)
        {
           Employee emp = employeeRepo.findById(employeeDto.getManagerId()).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
           employee.setManager(emp);
        }

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

            Set<UUID> gameIds = profileDto.getGamePreferences();
            List<Game> games = gameRepo.findAllById(gameIds);

            if(games.size() != gameIds.size())
            {
                throw new IllegalArgumentException("Games not found");
            }

            employee.setGamePreferences(new HashSet<>(games));
        }

        employeeRepo.save(employee);
    }
}
