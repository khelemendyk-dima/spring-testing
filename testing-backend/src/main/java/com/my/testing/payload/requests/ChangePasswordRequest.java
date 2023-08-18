package com.my.testing.payload.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * ChangePasswordRequest class. Used for changing the user's old password
 * with a new one.
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "Old password must not be empty")
    @Schema(description = "Old password", example = "iAmOldPass")
    private String oldPassword;

    @NotBlank(message = "New password must not be empty")
    @Schema(description = "New password", example = "iAmTheNewOne")
    private String newPassword;
}
