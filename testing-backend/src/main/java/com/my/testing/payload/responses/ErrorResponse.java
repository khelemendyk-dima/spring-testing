package com.my.testing.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private long timestamp;
    private String message;
}
