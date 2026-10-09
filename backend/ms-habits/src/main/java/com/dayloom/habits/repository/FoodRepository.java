package com.dayloom.habits.repository;

import com.dayloom.habits.model.FoodEntry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepository extends MongoRepository<FoodEntry, String> {
  List<FoodEntry> findByUserIdAndDateOrderByTimeAsc(String userId, String date);
  List<FoodEntry> findByUserId(String userId);
  Optional<FoodEntry> findByIdAndUserId(String id, String userId);
}
