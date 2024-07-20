package com.devmare.tradingbackend.business.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DefaultResponse {

    private Status status;
    private Map data;
    private String message;

    public DefaultResponse(Status status, String message) {
        this(status, new HashMap<>(), message);
    }

    public enum Status {
        FAILED,
        SUCCESS
    }
}