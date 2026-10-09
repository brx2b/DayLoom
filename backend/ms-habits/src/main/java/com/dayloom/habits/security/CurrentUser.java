package com.dayloom.habits.security;

import org.springframework.security.core.Authentication;

public final class CurrentUser {
  private CurrentUser() {}

  public static String userId(Authentication auth) {
    return (String) auth.getDetails();
  }
}
