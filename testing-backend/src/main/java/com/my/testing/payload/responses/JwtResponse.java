package com.my.testing.payload.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * JwtResponse class. Describes JWT tokens.
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    @Schema(description = "Access token", example = "strange-long-string")
    private String accessToken;

    @Schema(description = "Refresh token", example = "very-secret-string")
    private String refreshToken;
}
