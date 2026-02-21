package com.example.hrms_backend.configs;

import com.example.hrms_backend.dto.OrgChartEmployeeDto;
import com.example.hrms_backend.dto.TravelDocumentDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.TravelDocument;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                        .setAmbiguityIgnored(true);

        modelMapper.typeMap(TravelDocument.class, TravelDocumentDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getTravel().getTravelId(),
                            TravelDocumentDto::setTravelId);
                });

        modelMapper.typeMap(Employee.class, OrgChartEmployeeDto.class)
                .addMappings(mapper ->{
                    mapper.map(Employee::getEmployeeId,
                            OrgChartEmployeeDto::setEmployeeId);
                    mapper.map(Employee::getName,
                            OrgChartEmployeeDto::setName);
                    mapper.map(Employee::getDesignation,
                            OrgChartEmployeeDto::setDesignation);
                    mapper.map(Employee::getProfilePictureFileUrl,
                            OrgChartEmployeeDto::setProfilePictureFileUrl);
                });

        return modelMapper;
    }
}
