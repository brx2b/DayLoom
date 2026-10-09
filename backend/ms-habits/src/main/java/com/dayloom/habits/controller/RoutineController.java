package com.dayloom.habits.controller;

import com.dayloom.habits.dto.RoutineReq;
import com.dayloom.habits.model.Activity;
import com.dayloom.habits.model.Routine;
import com.dayloom.habits.security.CurrentUser;
import com.dayloom.habits.service.RoutineService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/time/routines")
public class RoutineController {
  private final RoutineService service;

  public RoutineController(RoutineService service) {
    this.service = service;
  }

  @GetMapping
  public List<Routine> list(Authentication auth) {
    return service.list(CurrentUser.userId(auth));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Routine create(Authentication auth, @Valid @RequestBody RoutineReq req) {
    return service.create(CurrentUser.userId(auth), req);
  }

  @GetMapping("/{id}")
  public Routine get(Authentication auth, @PathVariable String id) {
    return service.get(CurrentUser.userId(auth), id);
  }

  @PutMapping("/{id}")
  public Routine update(Authentication auth, @PathVariable String id,
      @Valid @RequestBody RoutineReq req) {
    return service.update(CurrentUser.userId(auth), id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(Authentication auth, @PathVariable String id) {
    service.delete(CurrentUser.userId(auth), id);
  }

  @PostMapping("/{id}/apply-today")
  @ResponseStatus(HttpStatus.CREATED)
  public Activity applyToday(Authentication auth, @PathVariable String id) {
    return service.applyToday(CurrentUser.userId(auth), id);
  }
}
