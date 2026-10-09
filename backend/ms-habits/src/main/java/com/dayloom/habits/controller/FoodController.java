package com.dayloom.habits.controller;

import com.dayloom.habits.dto.FoodReq;
import com.dayloom.habits.model.FoodEntry;
import com.dayloom.habits.security.CurrentUser;
import com.dayloom.habits.service.FoodService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/api/food")
public class FoodController {
  private final FoodService service;

  public FoodController(FoodService service) {
    this.service = service;
  }

  @GetMapping
  public List<FoodEntry> list(Authentication auth,
      @RequestParam(required = false) String date) {
    String d = date == null ? LocalDate.now().toString() : date;
    return service.list(CurrentUser.userId(auth), d);
  }

  @GetMapping("/frequent")
  public List<Map<String, Object>> frequent(Authentication auth) {
    return service.frequent(CurrentUser.userId(auth));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public FoodEntry create(Authentication auth, @Valid @RequestBody FoodReq req) {
    return service.create(CurrentUser.userId(auth), req);
  }

  @GetMapping("/{id}")
  public FoodEntry get(Authentication auth, @PathVariable String id) {
    return service.get(CurrentUser.userId(auth), id);
  }

  @PutMapping("/{id}")
  public FoodEntry update(Authentication auth, @PathVariable String id,
      @Valid @RequestBody FoodReq req) {
    return service.update(CurrentUser.userId(auth), id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(Authentication auth, @PathVariable String id) {
    service.delete(CurrentUser.userId(auth), id);
  }
}
