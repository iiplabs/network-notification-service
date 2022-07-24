package com.iiplabs.nns.core.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NotificationWebSocketMessage {

    String webId;

    @JsonProperty("msisdnA")
    private String destinationPhone;

    @JsonProperty("msisdnB")
    private String sourcePhone;

    private NotificationStatus status;

    private String comments;

    private LocalDateTime timeStamp;

}
