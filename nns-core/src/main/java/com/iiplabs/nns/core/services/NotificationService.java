package com.iiplabs.nns.core.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.text.StringSubstitutor;
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
import com.iiplabs.nns.core.utils.DateTimeUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class NotificationService implements INotificationService {

  private final static long SMS_SERVICE_MAX_ATTEMPTS = 5;

  @Value("${notification.expiry_window_hours}")
  private long expiryWindowHours;

  @Value("${ping.error.repeat.interval.seconds}")
  private long pingErrorRepeatIntervalSeconds;

  @Value("${sms.send.window}")
  private String smsSendWindow;

  @Value("${sms.text}")
  private String textTemplate;

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
      simpMessagingTemplate.convertAndSend("/topic/ws", objectMapper.writeValueAsString(notification));
    } catch (MessagingException | JsonProcessingException e) {
      log.error("Error posting to Web Sockets");
    }

    // submit request to locate subscriber within the network
    eventBus.post(
        new NotifyEvent(notification.getWebId(), notification.getSourcePhone(),
            notification.getDestinationPhone()));

    return notification;
  }

  @Override
  public void notify(String webId, String sourcePhone, String destinationPhone) {
    log.info("Begin locating subscriber {}", sourcePhone);

    // divide total ping window by one interval value to get max attempts for fixed
    // delay strategy
    long maxAttempts = ((expiryWindowHours * 60 * 60) / pingErrorRepeatIntervalSeconds);

    pingServiceClient.send(null, sourcePhone, maxAttempts).subscribe(pingClientResponse -> {
      log.info("Subscriber {} located. Sending SMS notification", sourcePhone);
      String text = getNotificationBody(textTemplate);
      log.info("User message content: {}", text);

      long durationSeconds = getDelayUntilNextSmsSendWindow();
      if (durationSeconds > 0) {
        log.info("Delaying SMS message for {}, until the next window {}",
            DurationFormatUtils.formatDuration(durationSeconds * 1000, "HH'h' mm'm' ss's'", true), smsSendWindow);
      }

      // calc duration until tne next message window
      smsServiceClient.send(null, sourcePhone, destinationPhone, text, SMS_SERVICE_MAX_ATTEMPTS)
          .delaySubscription(Duration.ofSeconds(durationSeconds))
          .subscribe(smsClientResponse -> {
            notificationsRepository.deleteByWebId(webId);
            log.info("Removed notification request webId {}", webId);
          });
    });
  }

  /**
   * Replaces {$time} key with the current time
   * @param template
   * @return
   */
  private String getNotificationBody(String template) {
    Map<String, String> values = new HashMap<>();
    values.put("time", LocalDateTime.now().toString());

    StringSubstitutor substitutor = new StringSubstitutor(values, "{$", "}");

    return substitutor.replace(template);
  }

  /**
   * 
   * @return number of seconds to wait until the next window
   */
  private long getDelayUntilNextSmsSendWindow() {
    return DateTimeUtil.getDelayUntilNextSmsSendWindow(smsSendWindow, LocalDateTime.now());
  }

}
