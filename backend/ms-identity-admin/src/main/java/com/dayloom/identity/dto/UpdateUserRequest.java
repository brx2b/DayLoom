package com.dayloom.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @Size(max = 100) String name,
    @Email String email,
    @Size(min = 8, max = 100) String password,
    String role) {}
