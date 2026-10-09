package com.dayloom.habits.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("activities")
public class Activity {
  @Id
  private String id;
  private String userId;
  private String title;
  private String category;
  private String date;
  private String start;
  private String end;
  private Instant createdAt;

  public Activity() {}

  public String getId() { return id; }
  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getCategory() { return category; }
  public void setCategory(String category) { this.category = category; }
  public String getDate() { return date; }
  public void setDate(String date) { this.date = date; }
  public String getStart() { return start; }
  public void setStart(String start) { this.start = start; }
  public String getEnd() { return end; }
  public void setEnd(String end) { this.end = end; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
