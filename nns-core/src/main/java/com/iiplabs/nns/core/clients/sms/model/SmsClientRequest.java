package com.iiplabs.nns.core.clients.sms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SmsClientRequest {
    
    @JsonProperty("msisdnB")
    private String sourcePhone;

    @JsonProperty("msisdnA")
    private String destinationPhone;

    private String text;
    
}
