package com.workforce.dto;

import com.workforce.domain.enums.VacationRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacationRequestDto {

    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Builder.Default
    private VacationRequestStatus status = VacationRequestStatus.PENDING;

    private Long approvedById;
    private String reason;
    private String rejectionReason;
}
