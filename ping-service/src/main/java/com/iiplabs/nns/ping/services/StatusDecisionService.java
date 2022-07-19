package com.iiplabs.nns.ping.services;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class StatusDecisionService implements IStatusDecisionService {

  /**
   * This service only serves the purpose of mocking for unit tests
   */
  @Override
  public boolean getDecision() {
    Random random = new Random(System.nanoTime());
    return random.nextBoolean();
  }

}