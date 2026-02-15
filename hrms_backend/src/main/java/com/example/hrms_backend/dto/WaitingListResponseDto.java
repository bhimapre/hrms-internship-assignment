package com.example.hrms_backend.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class WaitingListResponseDto {

    private UUID timeSlotId;
    private UUID game_id;
}
