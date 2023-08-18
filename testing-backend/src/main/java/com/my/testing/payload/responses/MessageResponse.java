package com.my.testing.payload.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * MessageResponse class. Used for responses with 200 code.
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    @Schema(description = "String with good news", example = "Everything is fine!!!")
    private String message;
}
