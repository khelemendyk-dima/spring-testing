package com.my.testing.payload.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "Old password must not be empty")
    private String oldPassword;

    @NotBlank(message = "New password must not be empty")
    private String newPassword;
}
