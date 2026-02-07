package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.Employee;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class GameDto {
    private UUID gameId;
    private String gameName;
    private boolean active;

    @ManyToMany(mappedBy = "gamePreferences")
    private Set<UUID> interestedEmployeesIds;
}
