package com.my.testing.payload.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * RegisterRequest class. Used for registration in the system.
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@Getter
@Setter
public class RegisterRequest {
    @Email(message = "Email must be valid")
    @Size(min = 3, max = 255, message = "Email must be between 3 and 255 characters")
    @NotBlank(message = "Email must not be empty")
    @Schema(description = "User's email", example = "example@gmail.com")
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Schema(description = "User's password", example = "secretWord")
    private String password;

    @Size(min = 2, max = 35, message = "First name must be between 2 and 35 characters")
    @NotBlank(message = "First name must not be empty")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Size(min = 2, max = 35, message = "Last name must be between 2 and 35 characters")
    @NotBlank(message = "Last name must not be empty")
    @Schema(description = "User's last name", example = "Wick")
    private String lastName;
}
