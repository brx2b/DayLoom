package com.dayloom.identity.service;

import com.dayloom.identity.dto.UpdateUserRequest;
import com.dayloom.identity.dto.UserDto;
import com.dayloom.identity.model.AuditLog;
import com.dayloom.identity.model.Role;
import com.dayloom.identity.model.User;
import com.dayloom.identity.repository.AuditLogRepository;
import com.dayloom.identity.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminUserService {
  private final UserRepository users;
  private final AuditLogRepository audit;
  private final PasswordEncoder encoder;

  public AdminUserService(UserRepository users, AuditLogRepository audit, PasswordEncoder encoder) {
    this.users = users;
    this.audit = audit;
    this.encoder = encoder;
  }

  public List<UserDto> list() {
    return users.findAll().stream().map(UserDto::of).toList();
  }

  public UserDto get(String id) {
    return UserDto.of(find(id));
  }

  public UserDto update(String adminEmail, String id, UpdateUserRequest req) {
    User user = find(id);
    if (req.name() != null && !req.name().isBlank()) user.setName(req.name().trim());
    if (req.email() != null && !req.email().isBlank()) {
      String email = req.email().toLowerCase().trim();
      if (!email.equals(user.getEmail()) && users.existsByEmail(email)) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "email ya registrado");
      }
      user.setEmail(email);
    }
    if (req.password() != null && !req.password().isBlank()) {
      user.setPasswordHash(encoder.encode(req.password()));
    }
    if (req.role() != null && !req.role().isBlank()) {
      try {
        user.setRole(Role.valueOf(req.role().trim().toUpperCase()));
      } catch (IllegalArgumentException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rol invalido (USER/ADMIN)");
      }
    }
    users.save(user);
    audit.save(new AuditLog(adminEmail, "UPDATE_USER", id, clientIp()));
    return UserDto.of(user);
  }

  public void delete(String adminEmail, String id) {
    User user = find(id);
    if (user.getEmail().equalsIgnoreCase(adminEmail)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no puedes eliminar tu propia cuenta admin");
    }
    users.delete(user);
    audit.save(new AuditLog(adminEmail, "DELETE_USER", id, clientIp()));
  }

  private User find(String id) {
    return users.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario no existe"));
  }

  private String clientIp() {
    try {
      HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      return req.getRemoteAddr();
    } catch (Exception e) {
      return null;
    }
  }
}
