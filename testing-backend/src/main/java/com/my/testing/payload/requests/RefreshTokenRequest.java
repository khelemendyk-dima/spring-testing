package com.my.testing.payload.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token must not be empty")
    private String refreshToken;
}
