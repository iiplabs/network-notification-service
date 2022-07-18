package com.iiplabs.nns.core.model.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UnavailabeSubscriberResponseDto {

    private Map<String, String> errors;

    private UnavailableSubscriberStatus status = UnavailableSubscriberStatus.OK;

}
