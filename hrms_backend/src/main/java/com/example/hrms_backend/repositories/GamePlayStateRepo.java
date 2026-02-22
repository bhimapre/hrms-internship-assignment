package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.entities.GamePlayStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GamePlayStateRepo extends JpaRepository<GamePlayStats, UUID> {

    Optional<GamePlayStats> findByEmployeeAndGame(Employee employee, Game game);
}
