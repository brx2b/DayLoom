package com.dayloom.habits.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record RoutineReq(
    @NotBlank @Size(max = 120) String title,
    @Size(max = 60) String category,
    @NotEmpty List<Integer> daysOfWeek) {}
