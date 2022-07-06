package com.iiplabs.nns.ping.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiplabs.nns.ping.exceptions.PingServiceException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PingService implements IPingService {

  @Autowired
  private IStatusDecisionService statusDecisionService;

  @Override
  public void ping(String msisdn) {
    log.info(String.format("Looking up network status for %s", msisdn));

    if (!statusDecisionService.getDecision()) {
      String errorMessage = String.format("Network lookup failed for %s", msisdn);
      throw new PingServiceException(errorMessage);
    }

    log.info(String.format("Looking up network status for %s completed", msisdn));
  }

}
