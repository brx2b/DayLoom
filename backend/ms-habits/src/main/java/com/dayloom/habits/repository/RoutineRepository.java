package com.dayloom.habits.repository;

import com.dayloom.habits.model.Routine;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoutineRepository extends MongoRepository<Routine, String> {
  List<Routine> findByUserId(String userId);
  Optional<Routine> findByIdAndUserId(String id, String userId);
}
