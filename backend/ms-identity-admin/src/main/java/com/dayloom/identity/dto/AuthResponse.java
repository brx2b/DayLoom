package com.dayloom.identity.dto;

public record AuthResponse(String token, String email, String name, String role) {}
