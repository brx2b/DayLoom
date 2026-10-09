package com.dayloom.habits.controller;

import com.dayloom.habits.dto.ActivityReq;
import com.dayloom.habits.model.Activity;
import com.dayloom.habits.security.CurrentUser;
import com.dayloom.habits.service.ActivityService;
import jakarta.validation.Valid;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/time/activities")
public class ActivityController {
  private final ActivityService service;

  public ActivityController(ActivityService service) {
    this.service = service;
  }

  @GetMapping
  public List<Activity> list(Authentication auth,
      @RequestParam(required = false) String date) {
    String d = date == null ? LocalDate.now().toString() : date;
    return service.list(CurrentUser.userId(auth), d);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Activity create(Authentication auth, @Valid @RequestBody ActivityReq req) {
    return service.create(CurrentUser.userId(auth), req);
  }

  @GetMapping("/{id}")
  public Activity get(Authentication auth, @PathVariable String id) {
    return service.get(CurrentUser.userId(auth), id);
  }

  @PutMapping("/{id}")
  public Activity update(Authentication auth, @PathVariable String id,
      @Valid @RequestBody ActivityReq req) {
    return service.update(CurrentUser.userId(auth), id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(Authentication auth, @PathVariable String id) {
    service.delete(CurrentUser.userId(auth), id);
  }
}
