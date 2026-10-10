package com.dayloom.planner.repository;

import com.dayloom.planner.model.Expense;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
  List<Expense> findByUserIdAndDateStartingWithOrderByDateAsc(String userId, String month);
  Optional<Expense> findByIdAndUserId(String id, String userId);
}
