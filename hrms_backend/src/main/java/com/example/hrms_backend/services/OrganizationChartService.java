package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.EmployeeSearchDto;
import com.example.hrms_backend.dto.OrgChartEmployeeDto;
import com.example.hrms_backend.dto.OrgChartResponseDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationChartService {

    private final ModelMapper modelMapper;
    private final EmployeeRepo employeeRepo;
    private OrgChartEmployeeDto toOrgChartDto(Employee e){
        return new OrgChartEmployeeDto(
                e.getEmployeeId(),
                e.getName(),
                e.getDesignation(),
                e.getProfilePictureFileUrl()
        );
    }

    // Get Manager Chain
    private List<OrgChartEmployeeDto> managerChain(Employee employee) {

        List<OrgChartEmployeeDto> chain = new ArrayList<>();
        Employee current = employee.getManager();

        while (current != null) {
            chain.add(toOrgChartDto(current));
            current = current.getManager();
        }

        Collections.reverse(chain);
        return chain;
    }

    // Get Direct Report Chain
    private List<OrgChartEmployeeDto> directReportsChain(Employee employee) {

        return employeeRepo
                .findByManager_EmployeeId(employee.getEmployeeId())
                .stream()
                .map(this::toOrgChartDto)
                .toList();
    }

    // Get Whole Chart Include Manager Chain And Direct Report Chain
    public OrgChartResponseDto getOrgChartByEmployeeId(UUID employeeId) {

        Employee selectedEmployee = employeeRepo.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

        OrgChartResponseDto response = new OrgChartResponseDto();

        response.setSelectedEmployee(
                toOrgChartDto(selectedEmployee)
        );

        response.setManagerChain(managerChain(selectedEmployee));

        response.setDirectReports(directReportsChain(selectedEmployee));

        return response;
    }

    // Build Current Chain For Login Employee
    public OrgChartResponseDto getMyOrgChart() {

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        return getOrgChartByEmployeeId(employeeId);
    }

    // Search Employees
    public List<EmployeeSearchDto> searchEmployees(String query) {
        return employeeRepo.serchEmployees(query)
                .stream()
                .map(emp -> modelMapper.map(emp, EmployeeSearchDto.class))
                .toList();
    }
}
