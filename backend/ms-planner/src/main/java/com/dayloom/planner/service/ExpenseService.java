package com.dayloom.planner.service;

import com.dayloom.planner.dto.ExpenseReq;
import com.dayloom.planner.model.Expense;
import com.dayloom.planner.repository.ExpenseRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExpenseService {
  private final ExpenseRepository repo;

  public ExpenseService(ExpenseRepository repo) {
    this.repo = repo;
  }

  public List<Expense> list(String userId, String month) {
    return repo.findByUserIdAndDateStartingWithOrderByDateAsc(userId, checkMonth(month));
  }

  public Expense create(String userId, ExpenseReq req) {
    Expense e = new Expense();
    e.setUserId(userId);
    e.setDate(req.date());
    e.setAmount(req.amount());
    e.setCategory(req.category().trim());
    e.setNote(req.note() == null ? null : req.note().trim());
    e.setCreatedAt(Instant.now());
    return repo.save(e);
  }

  public Expense update(String userId, String id, ExpenseReq req) {
    Expense e = find(userId, id);
    e.setDate(req.date());
    e.setAmount(req.amount());
    e.setCategory(req.category().trim());
    e.setNote(req.note() == null ? null : req.note().trim());
    return repo.save(e);
  }

  public void delete(String userId, String id) {
    repo.delete(find(userId, id));
  }

  public Map<String, Object> summary(String userId, String month) {
    List<Expense> all = list(userId, month);
    BigDecimal total = all.stream()
        .map(Expense::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    Map<String, BigDecimal> byCategory = all.stream()
        .collect(Collectors.groupingBy(Expense::getCategory,
            Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));
    List<Map<String, Object>> rows = byCategory.entrySet().stream()
        .sorted(Map.Entry.<String, BigDecimal>comparingByValue(Comparator.reverseOrder()))
        .map(e -> Map.<String, Object>of("category", e.getKey(), "total", e.getValue()))
        .toList();
    Map<String, Object> out = new LinkedHashMap<>();
    out.put("month", checkMonth(month));
    out.put("total", total);
    out.put("count", all.size());
    out.put("byCategory", rows);
    return out;
  }

  public Expense find(String userId, String id) {
    return repo.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "gasto no existe"));
  }

  static String checkMonth(String month) {
    String m = month == null ? LocalDate.now().toString().substring(0, 7) : month;
    if (!m.matches("^\\d{4}-\\d{2}$")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "month debe ser YYYY-MM");
    }
    return m;
  }
}
