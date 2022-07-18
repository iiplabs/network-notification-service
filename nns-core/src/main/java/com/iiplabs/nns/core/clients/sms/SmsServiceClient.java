package com.iiplabs.nns.core.clients.sms;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.iiplabs.nns.core.clients.sms.model.SmsClientRequest;
import com.iiplabs.nns.core.clients.sms.model.SmsClientResponse;
import com.iiplabs.nns.core.exceptions.NotificationServiceException;

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

        private final WebClient webClient;

        @Value("${sms.url}")
        private String smsServiceEndpoint;

        @Value("${sms.error.repeat.interval.seconds}")
        private long smsServiceRepeatIntervalSeconds;

        private static final long DEFAULT_TIMEOUT_SECONDS = 15;

        public SmsServiceClient(WebClient.Builder webClientBuilder) {
                HttpClient httpClient = HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) DEFAULT_TIMEOUT_SECONDS * 1000)
                                .responseTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS))
                                .doOnConnected(
                                                conn -> conn.addHandlerLast(new ReadTimeoutHandler(
                                                                DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                                                                .addHandlerLast(new WriteTimeoutHandler(
                                                                                DEFAULT_TIMEOUT_SECONDS,
                                                                                TimeUnit.SECONDS)));

                this.webClient = webClientBuilder
                                .baseUrl(smsServiceEndpoint)
                                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .clientConnector(new ReactorClientHttpConnector(httpClient))
                                .build();
        }

        @Override
        public Mono<SmsClientResponse> send(String authToken, String sourcePhone, String destinationPhone, String text,
                        long maxAttempts) {
                SmsClientRequest request = new SmsClientRequest();
                request.setSourcePhone(sourcePhone);
                request.setDestinationPhone(destinationPhone);
                request.setText(text);

                return webClient.post()
                                .headers(h -> h.setBearerAuth(authToken))
                                .body(Mono.just(request), SmsClientRequest.class)
                                .retrieve()
                                .bodyToMono(SmsClientResponse.class)
                                .doOnError(error -> log.error("SMS Service returned error: {}", error.getMessage()))
                                .retryWhen(Retry.fixedDelay(maxAttempts,
                                                Duration.ofSeconds(smsServiceRepeatIntervalSeconds))
                                                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                                                        throw new NotificationServiceException(
                                                                        "Exhausted max retries to invoke SMS Service",
                                                                        HttpStatus.SERVICE_UNAVAILABLE.value());
                                                }));
        }

}
