package com.dayloom.planner.service;

import com.dayloom.planner.dto.NoteReq;
import com.dayloom.planner.model.Note;
import com.dayloom.planner.repository.NoteRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NoteService {
  private final NoteRepository repo;

  public NoteService(NoteRepository repo) {
    this.repo = repo;
  }

  public List<Note> list(String userId) {
    return repo.findByUserIdOrderByExpiresAtAsc(userId);
  }

  public List<Note> upcoming(String userId, int days) {
    if (days < 0 || days > 365) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "days debe estar entre 0 y 365");
    }
    String cutoff = LocalDateTime.now().plusDays(days).toString().substring(0, 16);
    return repo.findByUserIdOrderByExpiresAtAsc(userId).stream()
        .filter(n -> !n.isDone() && n.getExpiresAt().compareTo(cutoff) <= 0)
        .toList();
  }

  public Note create(String userId, NoteReq req) {
    Note n = new Note();
    n.setUserId(userId);
    n.setTitle(req.title().trim());
    n.setBody(req.body() == null ? null : req.body().trim());
    n.setExpiresAt(req.expiresAt());
    n.setDone(req.done());
    n.setCreatedAt(Instant.now());
    return repo.save(n);
  }

  public Note update(String userId, String id, NoteReq req) {
    Note n = find(userId, id);
    n.setTitle(req.title().trim());
    n.setBody(req.body() == null ? null : req.body().trim());
    n.setExpiresAt(req.expiresAt());
    n.setDone(req.done());
    return repo.save(n);
  }

  public void delete(String userId, String id) {
    repo.delete(find(userId, id));
  }

  public Note find(String userId, String id) {
    return repo.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "nota no existe"));
  }
}
