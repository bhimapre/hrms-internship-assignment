package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TravelRepo extends JpaRepository<Travel, UUID> {
}
