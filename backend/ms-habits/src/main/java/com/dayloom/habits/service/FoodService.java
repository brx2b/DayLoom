package com.dayloom.habits.service;

import com.dayloom.habits.dto.FoodReq;
import com.dayloom.habits.model.FoodEntry;
import com.dayloom.habits.repository.FoodRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FoodService {
  private final FoodRepository repo;

  public FoodService(FoodRepository repo) {
    this.repo = repo;
  }

  public List<FoodEntry> list(String userId, String date) {
    return repo.findByUserIdAndDateOrderByTimeAsc(userId, date);
  }

  public FoodEntry get(String userId, String id) {
    return find(userId, id);
  }

  public FoodEntry create(String userId, FoodReq req) {
    FoodEntry f = new FoodEntry();
    f.setUserId(userId);
    f.setDate(req.date());
    f.setTime(req.time());
    f.setName(req.name().trim());
    f.setQuantity(req.quantity() == null ? null : req.quantity().trim());
    f.setType(req.type());
    f.setCreatedAt(Instant.now());
    return repo.save(f);
  }

  public FoodEntry update(String userId, String id, FoodReq req) {
    FoodEntry f = find(userId, id);
    f.setDate(req.date());
    f.setTime(req.time());
    f.setName(req.name().trim());
    f.setQuantity(req.quantity() == null ? null : req.quantity().trim());
    f.setType(req.type());
    return repo.save(f);
  }

  public void delete(String userId, String id) {
    repo.delete(find(userId, id));
  }

  public List<Map<String, Object>> frequent(String userId) {
    return repo.findByUserId(userId).stream()
        .collect(Collectors.groupingBy(f -> f.getName().toLowerCase(), Collectors.counting()))
        .entrySet().stream()
        .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
        .limit(10)
        .map(e -> Map.<String, Object>of("name", e.getKey(), "count", e.getValue()))
        .toList();
  }

  private FoodEntry find(String userId, String id) {
    return repo.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comida no existe"));
  }
}
