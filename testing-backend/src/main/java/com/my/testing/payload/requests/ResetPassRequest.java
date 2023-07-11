package com.my.testing.payload.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetPassRequest {
    @Size(min = 36, max = 36, message = "Invalid reset token")
    @NotBlank(message = "Token must not be empty")
    private String token;
    @NotBlank(message = "Password must not be empty")
    private String password;
}
