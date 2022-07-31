package com.iiplabs.nns.core.clients;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iiplabs.nns.core.clients.sms.SmsServiceClient;
import com.iiplabs.nns.core.clients.sms.model.SendMessageStatus;
import com.iiplabs.nns.core.clients.sms.model.SmsClientRequest;
import com.iiplabs.nns.core.clients.sms.model.SmsClientResponse;
import com.iiplabs.nns.core.controllers.TestApplicationContextInitializer;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SmsClientIntegrationTest {

    static MockWebServer mockBackEnd;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SmsServiceClient smsServiceClient;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry r) throws IOException {
        r.add("sms.url", () -> "http://localhost:" + mockBackEnd.getPort() + "/api/v1/sendSms");
    }

    @Test
    void testPostSmsOk() throws Exception {
        SmsClientRequest smsClientRequest = getSmsClientRequest("0");

        mockBackEnd.enqueue(
                new MockResponse().setBody(objectMapper.writeValueAsString(getSmsClientResponse(SendMessageStatus.OK)))
                        .addHeader("Content-Type", "application/json"));

        Mono<SmsClientResponse> smsClientResponse = smsServiceClient.send(null, UUID.randomUUID().toString(),
                smsClientRequest.getSourcePhone(),
                smsClientRequest.getDestinationPhone(), smsClientRequest.getText(), 0);

        StepVerifier.create(smsClientResponse)
                .expectNextMatches(clientResponse -> clientResponse.getStatus().equals(SendMessageStatus.OK))
                .verifyComplete();

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/api/v1/sendSms", recordedRequest.getPath());
    }

    @Test
    void testPostSmsServiceFail() throws Exception {
        SmsClientRequest smsClientRequest = getSmsClientRequest("0");

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(getSmsClientResponse(SendMessageStatus.FAIL)))
                .addHeader("Content-Type", "application/json"));

        Mono<SmsClientResponse> smsClientResponse = smsServiceClient.send(null, UUID.randomUUID().toString(),
                smsClientRequest.getSourcePhone(),
                smsClientRequest.getDestinationPhone(), smsClientRequest.getText(), 0);

        StepVerifier.create(smsClientResponse)
                .expectNextMatches(clientResponse -> clientResponse.getStatus().equals(SendMessageStatus.FAIL))
                .verifyComplete();

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/api/v1/sendSms", recordedRequest.getPath());
    }

    private SmsClientRequest getSmsClientRequest(String sourcePhone) {
        SmsClientRequest request = new SmsClientRequest();
        request.setSourcePhone(sourcePhone);
        request.setDestinationPhone("1");
        request.setText("test");
        return request;
    }

    private SmsClientResponse getSmsClientResponse(SendMessageStatus status) {
        SmsClientResponse response = new SmsClientResponse();
        response.setStatus(status);
        return response;
    }

}
