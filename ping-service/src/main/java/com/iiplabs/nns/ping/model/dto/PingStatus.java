package com.iiplabs.nns.ping.model.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PingStatus {

  IN_NETWORK("inNetwork"),
  UNAVAILABLE_SUBSCRIBER("unavailableSubscriber");

  private final String value;

  PingStatus(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}
