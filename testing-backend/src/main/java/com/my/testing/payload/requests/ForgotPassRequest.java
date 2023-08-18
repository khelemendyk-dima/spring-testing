package com.my.testing.payload.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * ForgotPassRequest class. Used for sending reset pass link to some email.
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@Getter
@Setter
public class ForgotPassRequest {
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be empty")
    @Schema(description = "User's email", example = "example@gmail.com")
    private String email;
}
