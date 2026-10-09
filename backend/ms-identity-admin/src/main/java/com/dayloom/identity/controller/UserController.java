package com.dayloom.identity.controller;

import com.dayloom.identity.model.User;
import com.dayloom.identity.repository.UserRepository;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserRepository users;

  public UserController(UserRepository users) {
    this.users = users;
  }

  @GetMapping("/me")
  public Map<String, String> me(Authentication auth) {
    User user = users.findByEmail(auth.getName())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return Map.of(
        "id", user.getId(),
        "email", user.getEmail(),
        "name", user.getName(),
        "role", user.getRole().name());
  }
}
