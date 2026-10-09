package com.dayloom.identity.dto;

import com.dayloom.identity.model.User;
import java.time.Instant;

public record UserDto(String id, String email, String name, String role, Instant createdAt) {
  public static UserDto of(User u) {
    return new UserDto(u.getId(), u.getEmail(), u.getName(), u.getRole().name(), u.getCreatedAt());
  }
}
