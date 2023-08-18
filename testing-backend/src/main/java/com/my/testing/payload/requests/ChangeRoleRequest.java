package com.my.testing.payload.requests;

import com.my.testing.models.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * ChangeRoleRequest class. Used for changing user's role.
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@Getter
@Setter
public class ChangeRoleRequest {
    @NotNull(message = "Role must not be null")
    @Schema(description = "User's new role", example = "ROLE_BLOCKED")
    private Role role;
}
