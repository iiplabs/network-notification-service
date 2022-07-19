package com.iiplabs.nns.core.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.AsyncEventBus;
import com.iiplabs.nns.core.clients.ping.IPingServiceClient;
import com.iiplabs.nns.core.clients.sms.ISmsServiceClient;
import com.iiplabs.nns.core.events.NotifyEvent;
import com.iiplabs.nns.core.model.Notification;
import com.iiplabs.nns.core.model.dto.UnavailabeSubscriberRequestDto;
import com.iiplabs.nns.core.reps.INotificationRepository;
import com.iiplabs.nns.core.utils.DaoFactory;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class NotificationService implements INotificationService {

  private final static long SMS_SERVICE_MAX_ATTEMPTS = 5;

  @Value("${notification.expiry_window_hours}")
  private long expiryWindowHours;

  @Value("${ping.error.repeat.interval.seconds}")
  private long pingErrorRepeatIntervalSeconds;

  @Autowired
  private INotificationRepository notificationsRepository;

  @Autowired
  private IPingServiceClient pingServiceClient;

  @Autowired
  private ISmsServiceClient smsServiceClient;

  @Autowired
  private AsyncEventBus eventBus;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @Override
  @Transactional
  public Notification create(UnavailabeSubscriberRequestDto unavailabeSubscriberRequestDto) {
    Notification notification = DaoFactory.newNotification(unavailabeSubscriberRequestDto, expiryWindowHours);
    notification = notificationsRepository.save(notification);

    log.info("New subscriber notification request created with webId {}", notification.getWebId());

    try {
      simpMessagingTemplate.convertAndSend("/topic/api/v1/messages", objectMapper.writeValueAsString(notification));
    } catch (MessagingException | JsonProcessingException e) {
      log.error("Error posting to Web Sockets");
    }

    // submit request to locate subscriber within the network
    // eventBus.post(
    // new NotifyEvent(notification.getWebId(), notification.getSourcePhone(),
    // notification.getDestinationPhone()));

    return notification;
  }

  @Override
  public void notify(String webId, String sourcePhone, String destinationPhone) {
    log.info("Begin locating subscriber {}", sourcePhone);

    // divide total ping window by one interval value to get max attempts for fixed
    // delay strategy
    long maxAttempts = ((expiryWindowHours * 60 * 60) / pingErrorRepeatIntervalSeconds);

    pingServiceClient.send(null, sourcePhone, maxAttempts).subscribe(pingClientResponse -> {
      log.info("Subscriber {} located. Sending SMS notification");
      String text = "";
      smsServiceClient.send(null, sourcePhone, destinationPhone, text, SMS_SERVICE_MAX_ATTEMPTS)
          .subscribe(smsClientResponse -> {
            notificationsRepository.deleteByWebId(webId);
            log.info("Removed notification request webId {}", webId);
          });
    });
  }

}
