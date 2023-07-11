package com.my.testing.payload.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ForgotPassRequest {
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be empty")
    private String email;
}
