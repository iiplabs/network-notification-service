package com.iiplabs.nns.sms.services;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.iiplabs.nns.sms.exceptions.SmsServiceException;
import com.iiplabs.nns.sms.model.dto.SendMessageRequestDto;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SmsService implements ISmsService {

  private static final int MAX_EXECUTION_DELAY = 5;

  @Override
  public void send(SendMessageRequestDto sendMessageRequestDto) {
    log.info(String.format("Sending SMS to %s that %s is now available", sendMessageRequestDto.getDestinationPhone(),
        sendMessageRequestDto.getSourcePhone()));
    Random random = new Random(System.nanoTime());

    try {
      TimeUnit.SECONDS.sleep(random.nextInt(MAX_EXECUTION_DELAY));
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      throw new SmsServiceException(ie.getMessage());
    }

    log.info(String.format("Success sending SMS to %s", sendMessageRequestDto.getDestinationPhone()));
  }

}
