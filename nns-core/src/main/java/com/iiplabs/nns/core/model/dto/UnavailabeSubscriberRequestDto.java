package com.iiplabs.nns.core.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UnavailabeSubscriberRequestDto implements Serializable {

    @NotNull(message = "{validation.invalid_dest_phone_num}")
    @Pattern(regexp = "^\\d{11}$", message = "{validation.invalid_dest_phone_num}")
    @JsonProperty("msisdnA")
    private String destinationPhone;

    @NotNull(message = "{validation.invalid_source_phone_num}")
    @Pattern(regexp = "^\\d{11}$", message = "{validation.invalid_source_phone_num}")
    @JsonProperty("msisdnB")
    private String sourcePhone;

}
