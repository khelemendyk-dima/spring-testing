package com.my.testing.payload.responses;

import com.my.testing.dtos.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * AuthResponse class. Used when user login or register in the system.
 * Describes JWT tokens and users info
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@Getter
@Setter
@Builder
public class AuthResponse {
    @Schema(description = "Access token", example = "strange-long-string")
    private String accessToken;

    @Schema(description = "Refresh token", example = "very-secret-string")
    private String refreshToken;
    private UserDTO user;
}
