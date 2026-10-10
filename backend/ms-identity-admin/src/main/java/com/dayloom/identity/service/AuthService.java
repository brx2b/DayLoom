package com.dayloom.identity.service;

import com.dayloom.identity.dto.AuthResponse;
import com.dayloom.identity.dto.LoginRequest;
import com.dayloom.identity.dto.RegisterRequest;
import com.dayloom.identity.model.Role;
import com.dayloom.identity.model.User;
import com.dayloom.identity.repository.UserRepository;
import com.dayloom.identity.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final JwtService jwt;

  public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
    this.users = users;
    this.encoder = encoder;
    this.jwt = jwt;
  }

  public AuthResponse register(RegisterRequest req) {
    String email = req.email().toLowerCase().trim();
    if (users.existsByEmail(email)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "email ya registrado");
    }
    User user = new User(email, encoder.encode(req.password()), req.name().trim(), Role.USER);
    users.save(user);
    return new AuthResponse(jwt.generate(user), user.getEmail(), user.getName(), user.getRole().name());
  }

  public AuthResponse login(LoginRequest req) {
    User user = checkCredentials(req);
    if (user.getRole() == Role.ADMIN) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN,
          "cuenta admin: usa el acceso admin desde Tailnet");
    }
    return new AuthResponse(jwt.generate(user), user.getEmail(), user.getName(), user.getRole().name());
  }

  public AuthResponse adminLogin(LoginRequest req) {
    User user = checkCredentials(req);
    if (user.getRole() != Role.ADMIN) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "acceso solo para administradores");
    }
    return new AuthResponse(jwt.generate(user), user.getEmail(), user.getName(), user.getRole().name());
  }

  private User checkCredentials(LoginRequest req) {
    String email = req.email().toLowerCase().trim();
    User user = users.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "credenciales invalidas"));
    if (!encoder.matches(req.password(), user.getPasswordHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "credenciales invalidas");
    }
    return user;
  }
}
