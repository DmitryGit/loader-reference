package com.example.loader.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("loaders")
public class Loader {
    @Id
    private Long id;
    private String brand;
    private String number;
    private BigDecimal capacity;
    private Boolean active = true;
    private LocalDateTime lastModified;
    private String modifiedBy;
}