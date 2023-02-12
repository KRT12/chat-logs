package com.example.chatlogs.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class AddMessageRequest {

    @NotNull(message = "Please enter a Message")
    @NotBlank(message = "Message cannot be blank")
    @JsonProperty("message")
    private String message;

    @NotNull(message = "timestamp is mandatory")
    @JsonProperty("timestamp")
    private Long timestamp;

    @NotNull(message = "is_sent is mandatory")
    @JsonProperty("is_sent")
    private Boolean isSent;
}
