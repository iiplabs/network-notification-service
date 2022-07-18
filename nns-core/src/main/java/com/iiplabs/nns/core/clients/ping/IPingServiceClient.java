package com.iiplabs.nns.core.clients.ping;

import com.iiplabs.nns.core.clients.ping.model.PingClientResponse;

import reactor.core.publisher.Mono;

public interface IPingServiceClient {

    Mono<PingClientResponse> send(String authToken, String sourcePhone, long maxAttempts);

}
