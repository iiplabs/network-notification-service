package com.iiplabs.nns.core.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iiplabs.nns.core.model.dto.UnavailabeSubscriberRequestDto;

@ActiveProfiles("test")
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private NotificationController notificationController;

  @Autowired
  private ObjectMapper objectMapper;

  final private String testJsonUnavailabeSubscriberRequestDto = "{\"msisdnA\":\"79063332211\",\"msisdnB\":\"79031112233\"}";
  final private String testJsonUnavailabeSubscriberRequestDtoEmptyPhone = "{\"msisdnB\":\"79031112233\",\"msisdnA\":\"\"}";
  final private String testJsonUnavailabeSubscriberRequestDtoNoDestPhoneField = "{\"msisdnB\":\"79031112233\"}";
  
  final private String NOTIFY_SUBSCRIBER_END_POINT = "/api/v1/unavailableSubscriber";

  @BeforeAll
  public static void setup() {
    //
  }

  @Test
  public void contextLoads() {
    //
  }

  @Test
  public void whenNotificationsControllerInjected_thenNotNull() throws Exception {
    assertThat(notificationController).isNotNull();
  }

  @Test
  public void testUnavailableSubscriberHappyPath() throws Exception {
    mockMvc.perform(post(NOTIFY_SUBSCRIBER_END_POINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(testJsonUnavailabeSubscriberRequestDto)
        .header("X-nns-core-api-key", "regular-test-1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status", any(String.class)))
        .andDo(print());
  }

  @Test
  public void testUnavailableSubscriberEmptyPhone() throws Exception {
    mockMvc.perform(post(NOTIFY_SUBSCRIBER_END_POINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(testJsonUnavailabeSubscriberRequestDtoEmptyPhone)
        .header("X-nns-core-api-key", "regular-test-1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", any(String.class)))
        .andExpect(jsonPath("$.errors", notNullValue()))
        .andDo(print());
  }

  @Test
  public void testUnavailableSubscriberMissingPhone() throws Exception {
    mockMvc.perform(post(NOTIFY_SUBSCRIBER_END_POINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(testJsonUnavailabeSubscriberRequestDtoNoDestPhoneField)
        .header("X-nns-core-api-key", "regular-test-1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", any(String.class)))
        .andExpect(jsonPath("$.errors", notNullValue()))
        .andDo(print());
  }

  @Test
  public void serializationTest() throws IOException {
    UnavailabeSubscriberRequestDto unavailabeSubscriberRequestDto = objectMapper.readValue(testJsonUnavailabeSubscriberRequestDto,
    UnavailabeSubscriberRequestDto.class);
    final String serializedPaymentDtoAsJson = objectMapper.writeValueAsString(unavailabeSubscriberRequestDto);
    assertEquals(serializedPaymentDtoAsJson, testJsonUnavailabeSubscriberRequestDto);
  }

  @Test
  public void deserializationTest() throws IOException {
    UnavailabeSubscriberRequestDto deserializedSendMessageRequestDto = objectMapper.readValue(testJsonUnavailabeSubscriberRequestDto,
    UnavailabeSubscriberRequestDto.class);
    UnavailabeSubscriberRequestDto sendMessageRequestDto = getUnavailabeSubscriberRequestDto();
    assertEquals(deserializedSendMessageRequestDto, sendMessageRequestDto);
  }

  private static UnavailabeSubscriberRequestDto getUnavailabeSubscriberRequestDto() {
    UnavailabeSubscriberRequestDto unavailabeSubscriberRequestDto = new UnavailabeSubscriberRequestDto();
    unavailabeSubscriberRequestDto.setSourcePhone("79031112233");
    unavailabeSubscriberRequestDto.setDestinationPhone("79063332211");
    return unavailabeSubscriberRequestDto;
  }

}
