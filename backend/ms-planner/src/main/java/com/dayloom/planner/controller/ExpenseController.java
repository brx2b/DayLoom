package com.dayloom.planner.controller;

import com.dayloom.planner.dto.ExpenseReq;
import com.dayloom.planner.model.Expense;
import com.dayloom.planner.security.CurrentUser;
import com.dayloom.planner.service.ExpenseService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/expenses")
public class ExpenseController {
  private final ExpenseService service;

  public ExpenseController(ExpenseService service) {
    this.service = service;
  }

  @GetMapping
  public List<Expense> list(Authentication auth,
      @RequestParam(required = false) String month) {
    return service.list(CurrentUser.userId(auth), month);
  }

  @GetMapping("/summary")
  public Map<String, Object> summary(Authentication auth,
      @RequestParam(required = false) String month) {
    return service.summary(CurrentUser.userId(auth), month);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Expense create(Authentication auth, @Valid @RequestBody ExpenseReq req) {
    return service.create(CurrentUser.userId(auth), req);
  }

  @GetMapping("/{id}")
  public Expense get(Authentication auth, @PathVariable String id) {
    return service.find(CurrentUser.userId(auth), id);
  }

  @PutMapping("/{id}")
  public Expense update(Authentication auth, @PathVariable String id,
      @Valid @RequestBody ExpenseReq req) {
    return service.update(CurrentUser.userId(auth), id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(Authentication auth, @PathVariable String id) {
    service.delete(CurrentUser.userId(auth), id);
  }
}
