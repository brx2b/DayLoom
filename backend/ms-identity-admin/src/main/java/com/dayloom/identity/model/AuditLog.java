package com.dayloom.identity.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("auditLogs")
public class AuditLog {
  @Id
  private String id;
  private String adminEmail;
  private String action;
  private String targetId;
  private String ip;
  private Instant at;

  public AuditLog() {}

  public AuditLog(String adminEmail, String action, String targetId, String ip) {
    this.adminEmail = adminEmail;
    this.action = action;
    this.targetId = targetId;
    this.ip = ip;
    this.at = Instant.now();
  }

  public String getId() { return id; }
  public String getAdminEmail() { return adminEmail; }
  public String getAction() { return action; }
  public String getTargetId() { return targetId; }
  public String getIp() { return ip; }
  public Instant getAt() { return at; }
}
