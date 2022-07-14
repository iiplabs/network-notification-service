package com.iiplabs.nns.core.clients.ping;

import com.iiplabs.nns.core.clients.ping.dto.PingClientResponseDto;

public interface IPingServiceClient {
    
    PingClientResponseDto send(String authToken, String sourcePhone);
    
}
