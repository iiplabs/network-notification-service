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
import com.iiplabs.nns.core.clients.ping.PingServiceClient;
import com.iiplabs.nns.core.clients.ping.model.PingClientResponse;
import com.iiplabs.nns.core.clients.ping.model.PingStatus;
import com.iiplabs.nns.core.controllers.TestApplicationContextInitializer;
import com.iiplabs.nns.core.exceptions.NotificationServiceException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PingClientIntegrationTest {

    static MockWebServer mockBackEnd;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PingServiceClient pingServiceClient;

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
        r.add("ping.url", () -> "http://localhost:" + mockBackEnd.getPort() + "/api/v1/ping");
    }

    @Test
    void testPingSubscriberOk() throws Exception {
        String sourcePhone = "0";

        mockBackEnd.enqueue(
                new MockResponse()
                        .setBody(objectMapper.writeValueAsString(getPingClientResponse(PingStatus.IN_NETWORK)))
                        .addHeader("Content-Type", "application/json"));

        Mono<PingClientResponse> pingClientResponse = pingServiceClient.send(null, UUID.randomUUID().toString(),
                sourcePhone, 0);

        StepVerifier.create(pingClientResponse)
                .expectNextMatches(clientResponse -> clientResponse.getStatus().equals(PingStatus.IN_NETWORK))
                .verifyComplete();

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/api/v1/ping?msisdn=" + sourcePhone, recordedRequest.getPath());
    }

    @Test
    void testPingSubscriberFail() throws Exception {
        String sourcePhone = "0";

        mockBackEnd.enqueue(
                new MockResponse()
                        .setBody(objectMapper
                                .writeValueAsString(getPingClientResponse(PingStatus.UNAVAILABLE_SUBSCRIBER)))
                        .addHeader("Content-Type", "application/json"));

        Mono<PingClientResponse> pingClientResponse = pingServiceClient.send(null, UUID.randomUUID().toString(),
                sourcePhone, 0);

        StepVerifier.create(pingClientResponse)
                .expectError(NotificationServiceException.class)
                .verify();

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/api/v1/ping?msisdn=" + sourcePhone, recordedRequest.getPath());
    }

    private PingClientResponse getPingClientResponse(PingStatus status) {
        PingClientResponse response = new PingClientResponse();
        response.setStatus(status);
        return response;
    }

}
