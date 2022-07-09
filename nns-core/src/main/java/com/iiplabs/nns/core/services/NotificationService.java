package com.iiplabs.nns.core.services;

import javax.transaction.Transactional;

import com.iiplabs.nns.core.model.dto.UnavailabeSubscriberRequestDto;
import com.iiplabs.nns.core.reps.INotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class NotificationService implements INotificationService {

  @Autowired
  private INotificationRepository notificationsRepository;

  @Override
  @Transactional
  public void create(UnavailabeSubscriberRequestDto unavailabeSubscriberRequestDto) {
    // notificationsRepository
  }

}
