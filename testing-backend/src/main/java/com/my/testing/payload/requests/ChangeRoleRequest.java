package com.my.testing.payload.requests;

import com.my.testing.models.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeRoleRequest {
    @NotNull(message = "Role must not be null")
    private Role role;
}
