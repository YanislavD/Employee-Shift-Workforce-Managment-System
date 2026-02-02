package com.workforce.dto;

import com.workforce.domain.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email
    @Size(max = 255)
    private String email;

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "Contract hours per week is required")
    @Min(1)
    @Max(168)
    private Integer contractHoursPerWeek;

    @Builder.Default
    private Boolean active = true;
}
