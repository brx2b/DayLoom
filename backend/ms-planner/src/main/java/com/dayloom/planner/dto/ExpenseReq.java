package com.dayloom.planner.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ExpenseReq(
    @NotBlank @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String date,
    @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
    @NotBlank @Size(max = 60) String category,
    @Size(max = 500) String note) {}
