package com.example.loader.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IncidentDto {
    private Long id;
    private Long loaderId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private String downtime;
}