package com.iiplabs.nns.core.clients.sms;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.iiplabs.nns.core.clients.sms.dto.SmsClientRequestDto;
import com.iiplabs.nns.core.clients.sms.dto.SmsClientResponseDto;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
public class SmsServiceClient implements ISmsServiceClient {

    private final static long DEFAULT_TIMEOUT = 30;

    @Value("${sms.url}")
    private String serviceEndpoint;

    @Override
    public SmsClientResponseDto send(String authToken, String sourcePhone, String destinationPhone, String text) {
        SmsClientRequestDto request = new SmsClientRequestDto();
        request.setSourcePhone(sourcePhone);
        request.setDestinationPhone(destinationPhone);
        request.setText(text);

        WebClient client = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return client.post()
                .uri(uriBuilder -> uriBuilder.path(serviceEndpoint).build())
                .headers(h -> h.setBearerAuth(authToken))
                .body(Mono.just(request), SmsClientRequestDto.class)
                .retrieve()
                .bodyToMono(SmsClientResponseDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block(Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

}
