package com.iiplabs.nns.sms.controllers;

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
import com.iiplabs.nns.sms.model.dto.SendMessageRequestDto;

@ActiveProfiles("test")
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SmsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private SmsController smsController;

  @Autowired
  private ObjectMapper objectMapper;

  final private String testJsonSendMessageRequestDto = "{\"text\":\"Этот абонент снова в сети\",\"msisdnB\":\"79031112233\",\"msisdnA\":\"79063332211\"}";
  final private String testJsonSendMessageRequestDtoEmptyPhone = "{\"msisdnB\":\"79031112233\",\"msisdnA\":\"\",\"text\":\"Этот абонент снова в сети\"}";
  final private String testJsonSendMessageRequestDtoNoDestPhoneField = "{\"msisdnB\":\"79031112233\",\"text\":\"Этот абонент снова в сети\"}";
  final private String testJsonSendMessageRequestDtoNoTextField = "{\"msisdnB\":\"79031112233\",\"msisdnA\":\"79063332211\"}";

  final private String SEND_SMS_END_POINT = "/api/v1/sendSms";

  @BeforeAll
  public static void setup() {
    //
  }

  @Test
  public void contextLoads() {
    //
  }

  @Test
  public void whenPaymentControllerInjected_thenNotNull() throws Exception {
    assertThat(smsController).isNotNull();
  }

  @Test
  public void testSendSmsHappyPath() throws Exception {
    mockMvc.perform(post(SEND_SMS_END_POINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(testJsonSendMessageRequestDto)
        .header("X-sms-service-api-key", "regular-test-1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status", any(String.class)))
        .andDo(print());
  }

  @Test
  public void testSendSmsEmptyPhone() throws Exception {
    mockMvc.perform(post(SEND_SMS_END_POINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(testJsonSendMessageRequestDtoEmptyPhone)
        .header("X-sms-service-api-key", "regular-test-1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").doesNotExist())
        .andExpect(jsonPath("$.errors", notNullValue()))
        .andDo(print());
  }

  @Test
  public void testSendSmsMissingPhone() throws Exception {
    mockMvc.perform(post(SEND_SMS_END_POINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(testJsonSendMessageRequestDtoNoDestPhoneField)
        .header("X-sms-service-api-key", "regular-test-1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").doesNotExist())
        .andExpect(jsonPath("$.errors", notNullValue()))
        .andDo(print());
  }

  @Test
  public void testSendSmsMissingText() throws Exception {
    mockMvc.perform(post(SEND_SMS_END_POINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(testJsonSendMessageRequestDtoNoTextField)
        .header("X-sms-service-api-key", "regular-test-1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").doesNotExist())
        .andExpect(jsonPath("$.errors", notNullValue()))
        .andDo(print());
  }

  @Test
  public void serializationTest() throws IOException {
    SendMessageRequestDto sendMessageRequestDto = objectMapper.readValue(testJsonSendMessageRequestDto,
        SendMessageRequestDto.class);
    final String serializedPaymentDtoAsJson = objectMapper.writeValueAsString(sendMessageRequestDto);
    assertEquals(serializedPaymentDtoAsJson, testJsonSendMessageRequestDto);
  }

  @Test
  public void deserializationTest() throws IOException {
    SendMessageRequestDto deserializedSendMessageRequestDto = objectMapper.readValue(testJsonSendMessageRequestDto,
        SendMessageRequestDto.class);
    SendMessageRequestDto sendMessageRequestDto = getMockSendMessageRequestDto();
    assertEquals(deserializedSendMessageRequestDto, sendMessageRequestDto);
  }

  private static SendMessageRequestDto getMockSendMessageRequestDto() {
    SendMessageRequestDto sendMessageRequestDto = new SendMessageRequestDto();
    sendMessageRequestDto.setSourcePhone("79031112233");
    sendMessageRequestDto.setDestinationPhone("79063332211");
    sendMessageRequestDto.setText("Этот абонент снова в сети");
    return sendMessageRequestDto;
  }

}
