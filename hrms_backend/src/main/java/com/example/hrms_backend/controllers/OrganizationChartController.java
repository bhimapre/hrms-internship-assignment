package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.EmployeeSearchDto;
import com.example.hrms_backend.dto.OrgChartResponseDto;
import com.example.hrms_backend.services.OrganizationChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrganizationChartController {

    private final OrganizationChartService organizationChartService;

    @GetMapping("/api/employees/search")
    public List<EmployeeSearchDto> searchEmployees(@RequestParam String query) {
        return organizationChartService.searchEmployees(query);
    }

    @GetMapping("/api/org-chart/{employeeId}")
    public OrgChartResponseDto getOrgChart(@PathVariable UUID employeeId) {
        return organizationChartService.getOrgChartByEmployeeId(employeeId);
    }

    @GetMapping("/api/org-chart/me")
    public OrgChartResponseDto getMyOrgChart() {
        return organizationChartService.getMyOrgChart();
    }
}
