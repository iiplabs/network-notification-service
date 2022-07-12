package com.iiplabs.nns.core.services;

import javax.transaction.Transactional;

import com.iiplabs.nns.core.model.Notification;
import com.iiplabs.nns.core.model.dto.UnavailabeSubscriberRequestDto;
import com.iiplabs.nns.core.reps.INotificationRepository;
import com.iiplabs.nns.core.utils.DaoFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class NotificationService implements INotificationService {

  @Value("${notification.expiry_window_hours}")
  private long expiryWindowHours;

  @Autowired
  private INotificationRepository notificationsRepository;

  @Override
  @Transactional
  public void create(UnavailabeSubscriberRequestDto unavailabeSubscriberRequestDto) {
    Notification notification = DaoFactory.newNotification(unavailabeSubscriberRequestDto, expiryWindowHours);
    notification = notificationsRepository.save(notification);
  }

}
