package com.iiplabs.nns.core.controllers;

import javax.validation.Valid;

import com.iiplabs.nns.core.annotations.RestControllerAnnotation;
import com.iiplabs.nns.core.model.dto.UnavailabeSubscriberRequestDto;
import com.iiplabs.nns.core.model.dto.UnavailabeSubscriberResponseDto;
import com.iiplabs.nns.core.services.INotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@RestControllerAnnotation
public class NotificationController {

  @Autowired
  private INotificationService notificationService;

  @PostMapping("/unavailableSubscriber")
  public ResponseEntity<UnavailabeSubscriberResponseDto> unavailableSubscriber(
      @Valid @RequestBody UnavailabeSubscriberRequestDto unavailabeSubscriberRequestDto) {
    notificationService.create(unavailabeSubscriberRequestDto);
    return ResponseEntity.ok(new UnavailabeSubscriberResponseDto());
  }

}
