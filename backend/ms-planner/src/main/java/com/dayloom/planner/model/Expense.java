package com.dayloom.planner.model;

import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("expenses")
public class Expense {
  @Id
  private String id;
  private String userId;
  private String date;
  private BigDecimal amount;
  private String category;
  private String note;
  private Instant createdAt;

  public Expense() {}

  public String getId() { return id; }
  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }
  public String getDate() { return date; }
  public void setDate(String date) { this.date = date; }
  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public String getCategory() { return category; }
  public void setCategory(String category) { this.category = category; }
  public String getNote() { return note; }
  public void setNote(String note) { this.note = note; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
