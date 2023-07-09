package com.my.testing.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {
    @Email(message = "Email must be valid")
    @Size(min = 3, max = 255, message = "Email must be between 3 and 255 characters")
    @NotBlank(message = "Email must not be empty")
    private String email;

    @NotBlank(message = "Password must not be empty")
    private String password;

    @Size(min = 2, max = 35, message = "First name must be between 2 and 35 characters")
    @NotBlank(message = "First name must not be empty")
    private String firstName;

    @Size(min = 2, max = 35, message = "Last name must be between 2 and 35 characters")
    @NotBlank(message = "Last name must not be empty")
    private String lastName;
}
