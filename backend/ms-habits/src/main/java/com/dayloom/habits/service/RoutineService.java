package com.dayloom.habits.service;

import com.dayloom.habits.dto.RoutineReq;
import com.dayloom.habits.model.Activity;
import com.dayloom.habits.model.Routine;
import com.dayloom.habits.repository.ActivityRepository;
import com.dayloom.habits.repository.RoutineRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RoutineService {
  private final RoutineRepository routines;
  private final ActivityRepository activities;

  public RoutineService(RoutineRepository routines, ActivityRepository activities) {
    this.routines = routines;
    this.activities = activities;
  }

  public List<Routine> list(String userId) {
    return routines.findByUserId(userId);
  }

  public Routine get(String userId, String id) {
    return find(userId, id);
  }

  public Routine create(String userId, RoutineReq req) {
    checkDays(req.daysOfWeek());
    Routine r = new Routine();
    r.setUserId(userId);
    r.setTitle(req.title().trim());
    r.setCategory(req.category() == null ? null : req.category().trim());
    r.setDaysOfWeek(req.daysOfWeek());
    r.setCreatedAt(Instant.now());
    return routines.save(r);
  }

  public Routine update(String userId, String id, RoutineReq req) {
    checkDays(req.daysOfWeek());
    Routine r = find(userId, id);
    r.setTitle(req.title().trim());
    r.setCategory(req.category() == null ? null : req.category().trim());
    r.setDaysOfWeek(req.daysOfWeek());
    return routines.save(r);
  }

  public void delete(String userId, String id) {
    routines.delete(find(userId, id));
  }

  public Activity applyToday(String userId, String id) {
    Routine r = find(userId, id);
    Activity a = new Activity();
    a.setUserId(userId);
    a.setTitle(r.getTitle());
    a.setCategory(r.getCategory());
    a.setDate(LocalDate.now().toString());
    a.setStart("09:00");
    a.setEnd("10:00");
    a.setCreatedAt(Instant.now());
    return activities.save(a);
  }

  private Routine find(String userId, String id) {
    return routines.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "rutina no existe"));
  }

  static void checkDays(List<Integer> days) {
    if (days == null || days.isEmpty() || days.stream().anyMatch(d -> d < 1 || d > 7)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "daysOfWeek debe tener valores 1-7");
    }
  }
}
