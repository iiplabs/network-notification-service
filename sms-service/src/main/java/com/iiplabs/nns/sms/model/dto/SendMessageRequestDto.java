package com.iiplabs.nns.sms.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SendMessageRequestDto implements Serializable {

    @NotNull(message = "{validation.invalid_source_phone}")
    @Pattern(regexp = "^\\d{11}$", message = "{validation.invalid_source_phone}")
    @JsonProperty("msisdnB")
    private String sourcePhone;

    @NotNull(message = "{validation.invalid_dest_phone}")
    @Pattern(regexp = "^\\d{11}$", message = "{validation.invalid_dest_phone}")
    @JsonProperty("msisdnA")
    private String destinationPhone;

    @NotNull(message = "{validation.invalid_sms_text}")
    @Size(min = 1, max = 100, message = "{validation.invalid_sms_text}")
    private String text;

}
