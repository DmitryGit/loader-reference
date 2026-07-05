package com.example.loader.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@Table("incidents")
public class Incident {
    @Id
    private Long id;
    private Long loaderId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
}