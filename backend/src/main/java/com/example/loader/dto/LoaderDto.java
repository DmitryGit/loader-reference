package com.example.loader.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoaderDto {
    private Long id;
    private String brand;
    private String number;
    private BigDecimal capacity;
    private Boolean active;
    private String lastModified;
    private String modifiedBy;
}