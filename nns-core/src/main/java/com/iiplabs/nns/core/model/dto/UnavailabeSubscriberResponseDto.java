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

    Map<String, String> errors;

    UnavailableSubscriberStatus status = UnavailableSubscriberStatus.OK;

}
