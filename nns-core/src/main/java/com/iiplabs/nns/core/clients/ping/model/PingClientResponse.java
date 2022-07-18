package com.iiplabs.nns.core.clients.ping.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PingClientResponse {

    private PingStatus status;

}
