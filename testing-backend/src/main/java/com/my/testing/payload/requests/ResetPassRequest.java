package com.my.testing.payload.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * ResetPassRequest class. Used for registration in the system.
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@Getter
@Setter
public class ResetPassRequest {
    @Size(min = 36, max = 36, message = "Invalid reset token")
    @NotBlank(message = "Token must not be empty")
    @Schema(description = "Token from the letter", example = "strange-long-string")
    private String token;

    @NotBlank(message = "Password must not be empty")
    @Schema(description = "New password", example = "newSecretWord")
    private String password;
}
