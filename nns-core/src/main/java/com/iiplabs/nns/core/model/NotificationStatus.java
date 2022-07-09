package com.iiplabs.nns.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum NotificationStatus {

  NEW(0),
  IN_PROGRESS(10),
  COMPLETED(20),
  EXPIRED(30);

  private final int code;

  NotificationStatus(int code) {
    this.code = code;
  }

  @JsonValue
  public int getCode() {
    return code;
  }

  @JsonCreator
  public static NotificationStatus findByCode(int code) {
    return Arrays.stream(NotificationStatus.values()).filter(i -> i.getCode() == code).findFirst().orElse(null);
  }

}
