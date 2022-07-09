package com.iiplabs.nns.core.controllers;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RateLimiterTest {

  @Autowired
  private MockMvc mockMvc;

  final private String testJsonUnavailabeSubscriberRequestDto = "{\"msisdnB\":\"79031112233\",\"msisdnA\":\"79063332211\"}";

  final private String NOTIFY_SUBSCRIBER_END_POINT = "/api/v1/unavailableSubscriber";

  @Test
  public void rateLimiterTest() throws Exception {
    IntStream.rangeClosed(1, 5)
        .boxed()
        .sorted(Collections.reverseOrder())
        .forEach(counter -> {
          successfulNotifySubscriberWebRequest(NOTIFY_SUBSCRIBER_END_POINT, counter - 1);
        });

    blockedNotifySubscriberWebRequestDueToRateLimit(NOTIFY_SUBSCRIBER_END_POINT);
  }

  private void successfulNotifySubscriberWebRequest(String url, Integer remainingTries) {
    try {
      mockMvc.perform(post(url)
          .contentType(MediaType.APPLICATION_JSON)
          .content(testJsonUnavailabeSubscriberRequestDto)
          .header("X-nns-core-api-key", "rate-limiter-test-1"))
          .andExpect(status().isOk())
          .andExpect(header().longValue("X-Rate-Limit-Remaining", remainingTries))
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.status", any(String.class)));
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  private void blockedNotifySubscriberWebRequestDueToRateLimit(String url) throws Exception {
    mockMvc
        .perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(testJsonUnavailabeSubscriberRequestDto)
            .header("X-nns-core-api-key", "rate-limiter-test-1"))
        .andExpect(status().isTooManyRequests());
  }

}
