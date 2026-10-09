package com.dayloom.habits.repository;

import com.dayloom.habits.model.Activity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityRepository extends MongoRepository<Activity, String> {
  List<Activity> findByUserIdAndDateOrderByStartAsc(String userId, String date);
  Optional<Activity> findByIdAndUserId(String id, String userId);
}
