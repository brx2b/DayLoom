package com.dayloom.habits.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("foodEntries")
public class FoodEntry {
  @Id
  private String id;
  private String userId;
  private String date;
  private String time;
  private String name;
  private String quantity;
  private MealType type;
  private Instant createdAt;

  public FoodEntry() {}

  public String getId() { return id; }
  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }
  public String getDate() { return date; }
  public void setDate(String date) { this.date = date; }
  public String getTime() { return time; }
  public void setTime(String time) { this.time = time; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getQuantity() { return quantity; }
  public void setQuantity(String quantity) { this.quantity = quantity; }
  public MealType getType() { return type; }
  public void setType(MealType type) { this.type = type; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
