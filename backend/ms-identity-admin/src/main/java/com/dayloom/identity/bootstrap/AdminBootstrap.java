package com.dayloom.identity.bootstrap;

import com.dayloom.identity.model.Role;
import com.dayloom.identity.model.User;
import com.dayloom.identity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Crea el admin inicial solo si ADMIN_EMAIL y ADMIN_PASSWORD vienen por entorno
 * y ese email no existe. Sin esas vars no hace nada.
 */
@Configuration
public class AdminBootstrap {
  @Bean
  ApplicationRunner seedAdmin(UserRepository users, PasswordEncoder encoder,
      @Value("${admin.email:}") String email,
      @Value("${admin.password:}") String password) {
    return args -> {
      if (email == null || email.isBlank() || password == null || password.isBlank()) return;
      String mail = email.toLowerCase().trim();
      if (users.existsByEmail(mail)) return;
      users.save(new User(mail, encoder.encode(password), "Admin", Role.ADMIN));
    };
  }
}
