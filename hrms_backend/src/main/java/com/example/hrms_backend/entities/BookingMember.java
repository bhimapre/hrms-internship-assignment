package com.example.hrms_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "booking_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"game_booking_id", "employee_id"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_member_id")
    private UUID bookingMemberId;

    @ManyToOne
    @JoinColumn(name = "game_booking_id", nullable = false)
    private GameBooking gameBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private UUID createdBy;
    private LocalDateTime createdAt;

}
