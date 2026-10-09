package com.dayloom.habits.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ActivityReq(
    @NotBlank @Size(max = 120) String title,
    @Size(max = 60) String category,
    @NotBlank @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String date,
    @NotBlank @Pattern(regexp = "^\\d{2}:\\d{2}$") String start,
    @NotBlank @Pattern(regexp = "^\\d{2}:\\d{2}$") String end) {}
