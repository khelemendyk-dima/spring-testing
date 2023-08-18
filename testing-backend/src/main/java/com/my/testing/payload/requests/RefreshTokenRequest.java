package com.my.testing.payload.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * RefreshTokenRequest class. Used for refreshing expired access token with a new one.
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@Getter
@Setter
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token must not be empty")
    @Schema(description = "Your refresh token", example = "very-secret-string")
    private String refreshToken;
}
