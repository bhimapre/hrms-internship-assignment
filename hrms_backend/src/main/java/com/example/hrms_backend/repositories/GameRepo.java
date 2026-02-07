package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepo extends JpaRepository<Game, UUID> {
}
