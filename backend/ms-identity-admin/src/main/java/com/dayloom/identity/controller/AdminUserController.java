package com.dayloom.identity.controller;

import com.dayloom.identity.dto.UpdateUserRequest;
import com.dayloom.identity.dto.UserDto;
import com.dayloom.identity.service.AdminUserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
  private final AdminUserService admin;

  public AdminUserController(AdminUserService admin) {
    this.admin = admin;
  }

  @GetMapping
  public List<UserDto> list() {
    return admin.list();
  }

  @GetMapping("/{id}")
  public UserDto get(@PathVariable String id) {
    return admin.get(id);
  }

  @PatchMapping("/{id}")
  public UserDto update(Authentication auth, @PathVariable String id,
      @Valid @RequestBody UpdateUserRequest req) {
    return admin.update(auth.getName(), id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(Authentication auth, @PathVariable String id) {
    admin.delete(auth.getName(), id);
  }
}
