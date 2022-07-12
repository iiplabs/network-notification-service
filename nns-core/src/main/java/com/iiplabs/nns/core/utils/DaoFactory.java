package com.iiplabs.nns.core.utils;

import java.time.LocalDateTime;
import java.util.UUID;

import com.iiplabs.nns.core.model.Notification;
import com.iiplabs.nns.core.model.NotificationStatus;
import com.iiplabs.nns.core.model.dto.UnavailabeSubscriberRequestDto;

public final class DaoFactory {

  private DaoFactory() {
    throw new AssertionError();
  }

  public static Notification newNotification(UnavailabeSubscriberRequestDto dto, long expiryWindowHours) {
    Notification notification = new Notification();
    notification.setCreated(LocalDateTime.now());
    notification.setDestinationPhone(dto.getDestinationPhone());
    notification.setExpiryDate(LocalDateTime.now().plusHours(expiryWindowHours));
    notification.setNextAttemptDate(LocalDateTime.now());
    notification.setNotificationStatus(NotificationStatus.NEW);
    notification.setSourcePhone(dto.getSourcePhone());
    notification.setWebId(UUID.randomUUID().toString());
    return notification;
  }

}
