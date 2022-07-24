package com.iiplabs.nns.core.clients.sms;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iiplabs.nns.core.clients.sms.model.SmsClientRequest;
import com.iiplabs.nns.core.clients.sms.model.SmsClientResponse;
import com.iiplabs.nns.core.exceptions.NotificationServiceException;
import com.iiplabs.nns.core.model.dto.NotificationStatus;
import com.iiplabs.nns.core.model.dto.NotificationWebSocketMessage;
import com.iiplabs.nns.core.utils.NnsObjectFactory;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

@Log4j2
@Component
public class SmsServiceClient implements ISmsServiceClient {

        private WebClient webClient;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private SimpMessagingTemplate simpMessagingTemplate;

        @Value("${sms.url}")
        private String smsServiceEndpoint;

        @Value("${sms.error.repeat.interval.seconds}")
        private long smsServiceRepeatIntervalSeconds;

        private static final long DEFAULT_TIMEOUT_SECONDS = 15;

        @Override
        public Mono<SmsClientResponse> send(String authToken, String webId, String sourcePhone, String destinationPhone,
                        String text,
                        long maxAttempts) {
                SmsClientRequest request = new SmsClientRequest();
                request.setSourcePhone(sourcePhone);
                request.setDestinationPhone(destinationPhone);
                request.setText(text);

                log.info("Sending (delayed) SMS notification to {}", destinationPhone);

                return webClient.post()
                                .headers(h -> h.setBearerAuth(authToken))
                                .body(Mono.just(request), SmsClientRequest.class)
                                .retrieve()
                                .bodyToMono(SmsClientResponse.class)
                                .doOnError(error -> {
                                        log.error("SMS Service returned error: {}", error.getMessage());

                                        NotificationWebSocketMessage webSocketMessage = NnsObjectFactory
                                                        .getNotificationWebSocketMessage(webId,
                                                                        NotificationStatus.SMS_FAILED);
                                        try {
                                                simpMessagingTemplate.convertAndSend("/topic/ws",
                                                                objectMapper.writeValueAsString(webSocketMessage));
                                        } catch (MessagingException | JsonProcessingException e) {
                                                log.error("Error posting to Web Sockets");
                                        }
                                })
                                .retryWhen(Retry.fixedDelay(maxAttempts,
                                                Duration.ofSeconds(smsServiceRepeatIntervalSeconds))
                                                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                                                        throw new NotificationServiceException(
                                                                        String.format("Exhausted max retries to invoke SMS Service to notify %s",
                                                                                        destinationPhone),
                                                                        HttpStatus.SERVICE_UNAVAILABLE.value());
                                                }));
        }

        @PostConstruct
        private void initClient() {
                HttpClient httpClient = HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) DEFAULT_TIMEOUT_SECONDS * 1000)
                                .responseTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS))
                                .doOnConnected(
                                                conn -> conn.addHandlerLast(new ReadTimeoutHandler(
                                                                DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                                                                .addHandlerLast(new WriteTimeoutHandler(
                                                                                DEFAULT_TIMEOUT_SECONDS,
                                                                                TimeUnit.SECONDS)));

                this.webClient = WebClient.builder()
                                .baseUrl(smsServiceEndpoint)
                                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .clientConnector(new ReactorClientHttpConnector(httpClient))
                                .build();

                log.info("SMS Service Client configured for base url {} and repeat interval {}s", smsServiceEndpoint,
                                smsServiceRepeatIntervalSeconds);
        }

}
