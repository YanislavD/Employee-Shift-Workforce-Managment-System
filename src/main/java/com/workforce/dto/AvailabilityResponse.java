package com.workforce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityResponse {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate date;
    private Boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
