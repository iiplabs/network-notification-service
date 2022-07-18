package com.iiplabs.nns.core.clients.sms;

import com.iiplabs.nns.core.clients.sms.model.SmsClientResponse;

import reactor.core.publisher.Mono;

public interface ISmsServiceClient {

    Mono<SmsClientResponse> send(String authToken, String sourcePhone, String destinationPhone, String text, long maxAttempts);

}
