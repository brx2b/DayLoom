package com.dayloom.planner.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final SecretKey key;

  public JwtService(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public Claims parse(String token) {
    var payload = Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token).getPayload();
    return new Claims(
        payload.getSubject(),
        (String) payload.get("userId"),
        (String) payload.get("role"));
  }

  public record Claims(String email, String userId, String role) {}
}
