package com.enzulode.dto;

public record EntityUpdateNotificationDto(NotificationType type, String message) {

  public EntityUpdateNotificationDto(NotificationType type) {
    this(type, type.getMessage());
  }

  public enum NotificationType {
    ADMIN_PROMOTION_ACCEPT("Your application for admin right was accepted!"),
    ADMIN_PROMOTION_DECLINE("Your application for admin right was rejected!"),
    ENTITY_CREATION("Some entities have been created!"),
    ENTITY_MODIFICATION("Some entities have been modified!"),
    ENTITY_DELETION("Some entities have been deleted!");

    private final String message;

    NotificationType(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }

    @Override
    public String toString() {
      return this.name();
    }
  }
}
