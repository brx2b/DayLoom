package com.dayloom.habits.dto;

import com.dayloom.habits.model.MealType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FoodReq(
    @NotBlank @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String date,
    @NotBlank @Pattern(regexp = "^\\d{2}:\\d{2}$") String time,
    @NotBlank @Size(max = 120) String name,
    @Size(max = 60) String quantity,
    @NotNull MealType type) {}
