package com.iiplabs.nns.core.clients.ping;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.iiplabs.nns.core.clients.ping.dto.PingClientResponseDto;

import reactor.util.retry.Retry;

@Component
public class PingServiceClient implements IPingServiceClient {

    private final static long DEFAULT_TIMEOUT = 30;

    @Value("${ping.url}")
    private String serviceEndpoint;

    @Override
    public PingClientResponseDto send(String authToken, String sourcePhone) {
        WebClient client = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return client.get()
                .uri(uriBuilder -> uriBuilder.path(serviceEndpoint).queryParam("msisdn", sourcePhone).build())
                .headers(h -> h.setBearerAuth(authToken))
                .retrieve()
                .bodyToMono(PingClientResponseDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block(Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

}
