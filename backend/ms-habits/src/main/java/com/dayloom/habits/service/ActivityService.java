package com.dayloom.habits.service;

import com.dayloom.habits.dto.ActivityReq;
import com.dayloom.habits.model.Activity;
import com.dayloom.habits.repository.ActivityRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ActivityService {
  private final ActivityRepository repo;

  public ActivityService(ActivityRepository repo) {
    this.repo = repo;
  }

  public List<Activity> list(String userId, String date) {
    return repo.findByUserIdAndDateOrderByStartAsc(userId, date);
  }

  public Activity create(String userId, ActivityReq req) {
    checkRange(req.start(), req.end());
    Activity a = new Activity();
    a.setUserId(userId);
    a.setTitle(req.title().trim());
    a.setCategory(req.category() == null ? null : req.category().trim());
    a.setDate(req.date());
    a.setStart(req.start());
    a.setEnd(req.end());
    a.setCreatedAt(Instant.now());
    return repo.save(a);
  }

  public Activity get(String userId, String id) {
    return find(userId, id);
  }

  public Activity update(String userId, String id, ActivityReq req) {
    checkRange(req.start(), req.end());
    Activity a = find(userId, id);
    a.setTitle(req.title().trim());
    a.setCategory(req.category() == null ? null : req.category().trim());
    a.setDate(req.date());
    a.setStart(req.start());
    a.setEnd(req.end());
    return repo.save(a);
  }

  public void delete(String userId, String id) {
    repo.delete(find(userId, id));
  }

  private Activity find(String userId, String id) {
    return repo.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "actividad no existe"));
  }

  static void checkRange(String start, String end) {
    if (end.compareTo(start) <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fin debe ser mayor que inicio");
    }
  }
}
