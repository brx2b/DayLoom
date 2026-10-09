package com.dayloom.identity.security;

import com.dayloom.identity.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final SecretKey key;
  private final long expirationMs;

  public JwtService(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-ms:86400000}") long expirationMs) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  public String generate(User user) {
    Date now = new Date();
    return Jwts.builder()
        .subject(user.getEmail())
        .claim("userId", user.getId())
        .claim("role", user.getRole().name())
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expirationMs))
        .signWith(key)
        .compact();
  }

  public String extractEmail(String token) {
    return Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token).getPayload().getSubject();
  }

  public String extractRole(String token) {
    return (String) Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token).getPayload().get("role");
  }

  public String extractUserId(String token) {
    return (String) Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token).getPayload().get("userId");
  }
}
