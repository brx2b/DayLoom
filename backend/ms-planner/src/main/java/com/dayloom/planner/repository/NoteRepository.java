package com.dayloom.planner.repository;

import com.dayloom.planner.model.Note;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {
  List<Note> findByUserIdOrderByExpiresAtAsc(String userId);
  Optional<Note> findByIdAndUserId(String id, String userId);
}
