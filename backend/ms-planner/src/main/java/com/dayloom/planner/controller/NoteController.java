package com.dayloom.planner.controller;

import com.dayloom.planner.dto.NoteReq;
import com.dayloom.planner.model.Note;
import com.dayloom.planner.security.CurrentUser;
import com.dayloom.planner.service.NoteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
  private final NoteService service;

  public NoteController(NoteService service) {
    this.service = service;
  }

  @GetMapping
  public List<Note> list(Authentication auth) {
    return service.list(CurrentUser.userId(auth));
  }

  @GetMapping("/upcoming")
  public List<Note> upcoming(Authentication auth,
      @RequestParam(defaultValue = "7") int days) {
    return service.upcoming(CurrentUser.userId(auth), days);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Note create(Authentication auth, @Valid @RequestBody NoteReq req) {
    return service.create(CurrentUser.userId(auth), req);
  }

  @GetMapping("/{id}")
  public Note get(Authentication auth, @PathVariable String id) {
    return service.find(CurrentUser.userId(auth), id);
  }

  @PutMapping("/{id}")
  public Note update(Authentication auth, @PathVariable String id,
      @Valid @RequestBody NoteReq req) {
    return service.update(CurrentUser.userId(auth), id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(Authentication auth, @PathVariable String id) {
    service.delete(CurrentUser.userId(auth), id);
  }
}
