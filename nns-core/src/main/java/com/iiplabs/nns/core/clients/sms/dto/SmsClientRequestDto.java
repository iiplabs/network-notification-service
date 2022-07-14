package com.iiplabs.nns.core.clients.sms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SmsClientRequestDto {
    
    @JsonProperty("msisdnB")
    private String sourcePhone;

    @JsonProperty("msisdnA")
    private String destinationPhone;

    private String text;
    
}
