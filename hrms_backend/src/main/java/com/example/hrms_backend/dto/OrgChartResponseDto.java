package com.example.hrms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgChartResponseDto {
    private OrgChartEmployeeDto selectedEmployee;
    private List<OrgChartEmployeeDto> managerChain;
    private List<OrgChartEmployeeDto> directReports;
}
