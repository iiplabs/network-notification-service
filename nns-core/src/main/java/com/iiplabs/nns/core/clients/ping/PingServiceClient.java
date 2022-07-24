package com.iiplabs.nns.core.clients.ping;

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
import com.iiplabs.nns.core.clients.ping.model.PingClientResponse;
import com.iiplabs.nns.core.clients.ping.model.PingStatus;
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
public class PingServiceClient implements IPingServiceClient {

    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Value("${ping.url}")
    private String pingServiceEndpoint;

    @Value("${ping.error.repeat.interval.seconds}")
    private long pingServiceRepeatIntervalSeconds;

    private static final long DEFAULT_TIMEOUT_SECONDS = 15;

    @Override
    public Mono<PingClientResponse> send(String authToken, String webId, String sourcePhone, long maxAttempts) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("msisdn", sourcePhone).build())
                .headers(h -> h.setBearerAuth(authToken))
                .retrieve()
                .bodyToMono(PingClientResponse.class)
                .onErrorResume(error -> Mono.just(new PingClientResponse(PingStatus.UNAVAILABLE_SUBSCRIBER)))
                .map(result -> {
                    if (result.getStatus() == null || PingStatus.UNAVAILABLE_SUBSCRIBER.equals(result.getStatus())) {
                        NotificationWebSocketMessage webSocketMessage = NnsObjectFactory
                                .getNotificationWebSocketMessage(webId,
                                        NotificationStatus.LOCATION_FAILED);
                        try {
                            simpMessagingTemplate.convertAndSend("/topic/ws",
                                    objectMapper.writeValueAsString(webSocketMessage));
                        } catch (MessagingException | JsonProcessingException e) {
                            log.error("Error posting to Web Sockets");
                        }

                        log.error("Subscriber {} unavailable", sourcePhone);
                        throw new NotificationServiceException(sourcePhone, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    }
                    return result;
                })
                .retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(pingServiceRepeatIntervalSeconds))
                        .filter(throwable -> throwable instanceof NotificationServiceException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new NotificationServiceException(
                                    String.format("Exhausted max retries to locate subscriber %s", sourcePhone),
                                    HttpStatus.SERVICE_UNAVAILABLE.value());
                        }));
    }

    @PostConstruct
    private void initClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) DEFAULT_TIMEOUT_SECONDS * 1000)
                .responseTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS))
                .doOnConnected(
                        conn -> conn.addHandlerLast(new ReadTimeoutHandler(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)));

        this.webClient = WebClient.builder()
                .baseUrl(pingServiceEndpoint)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        log.info("Ping Service Client configured for base url {}", pingServiceEndpoint,
                pingServiceRepeatIntervalSeconds);
    }

}
