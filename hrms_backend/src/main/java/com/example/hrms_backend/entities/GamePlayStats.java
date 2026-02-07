package com.example.hrms_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_play_stats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employee_id", "game_id"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GamePlayStats {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID statsId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "completed_slots")
    private int completedSlots;

    @Column(name = "last_played_at")
    private LocalDateTime lastPlayedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
