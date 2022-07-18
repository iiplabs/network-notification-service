package com.iiplabs.nns.core.services;

import com.iiplabs.nns.core.model.Notification;
import com.iiplabs.nns.core.model.dto.UnavailabeSubscriberRequestDto;

public interface INotificationService {

  Notification create(UnavailabeSubscriberRequestDto unavailabeSubscriberRequestDto);

  void notify(String webId, String sourcePhone, String destinationPhone);

}
