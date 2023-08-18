package com.my.testing.payload.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorResponse class. Used for responses that occurred while app is running
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@Getter
@AllArgsConstructor
public class ErrorResponse {
    @Schema(description = "Time of error", example = "1692371827219")
    private long timestamp;

    @Schema(description = "Message that describes some problem", example = "Something went wrong")
    private String message;
}


