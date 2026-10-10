package com.dayloom.planner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NoteReq(
    @NotBlank @Size(max = 120) String title,
    @Size(max = 2000) String body,
    @NotBlank @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}$") String expiresAt,
    boolean done) {}
