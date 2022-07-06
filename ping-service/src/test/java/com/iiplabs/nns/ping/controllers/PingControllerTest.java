package com.iiplabs.nns.ping.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iiplabs.nns.ping.model.dto.PingResponseDto;
import com.iiplabs.nns.ping.services.IStatusDecisionService;

@ActiveProfiles("test")
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PingController pingController;

  @MockBean
  private IStatusDecisionService statusDecisionService;

  @Autowired
  private ObjectMapper objectMapper;

  final private String testMsisdn = "79111001010";
  final private String testMsisdnInvalidZeroLength = "";
  final private String testMsisdnInvalidExtraLongLength = "79111001010111";
  final private String testMsisdnInvalidCharacters = "a9111001010";

  final private String testJsonPingResponseDto = "{\"status\":\"inNetwork\"}";

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
    assertThat(pingController).isNotNull();
  }

  @Test
  public void testPingHappyPath() throws Exception {
    _testPingHappyPath(true);
  }

  @Test
  public void testPingHappyPathUnavailableSubscriber() throws Exception {
    _testPingHappyPath(false);
  }

  private void _testPingHappyPath(boolean subscriberAvailable) throws Exception {
    when(statusDecisionService.getDecision()).thenReturn(subscriberAvailable);

    mockMvc.perform(get("/api/v1/ping").param("msisdn", testMsisdn))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status", any(String.class)))
        .andDo(print());
  }

  @Test
  public void testPingEmptyPhone() throws Exception {
    mockMvc.perform(get("/api/v1/ping").param("msisdn", testMsisdnInvalidZeroLength))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").doesNotExist())
        .andExpect(jsonPath("$.errors", notNullValue()))
        .andDo(print());
  }

  @Test
  public void testPingExtraLongPhone() throws Exception {
    mockMvc.perform(get("/api/v1/ping").param("msisdn", testMsisdnInvalidExtraLongLength))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").doesNotExist())
        .andExpect(jsonPath("$.errors", notNullValue()))
        .andDo(print());
  }

  @Test
  public void testPingInvalidCharacters() throws Exception {
    mockMvc.perform(get("/api/v1/ping").param("msisdn", testMsisdnInvalidCharacters))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").doesNotExist())
        .andExpect(jsonPath("$.errors", notNullValue()))
        .andDo(print());
  }

  @Test
  public void serializationTest() throws IOException {
    PingResponseDto pingResponseDto = new PingResponseDto();
    final String serializedPingResponseDto = objectMapper.writeValueAsString(pingResponseDto);
    assertEquals(testJsonPingResponseDto, serializedPingResponseDto);
  }

  @Test
  public void deserializationTest() throws IOException {
    PingResponseDto deserializedPingResponseDto = objectMapper.readValue(testJsonPingResponseDto,
        PingResponseDto.class);
    PingResponseDto pingResponseDto = getMockPingResponseDto();
    assertEquals(deserializedPingResponseDto, pingResponseDto);
  }

  private static PingResponseDto getMockPingResponseDto() {
    return new PingResponseDto();
  }

}
