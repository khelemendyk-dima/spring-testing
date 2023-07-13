package com.my.testing.controllers;

import com.my.testing.dtos.UserDTO;
import com.my.testing.models.enums.Role;
import com.my.testing.payload.requests.ChangePasswordRequest;
import com.my.testing.payload.requests.ChangeRoleRequest;
import com.my.testing.payload.responses.MessageResponse;
import com.my.testing.services.UserService;
import com.my.testing.utils.ConverterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ConverterUtil converterUtil;

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getById(@PathVariable("email") String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!email.equals(auth.getName()) && !isAdmin(auth))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO userDTO = converterUtil.convertToUserDTO(userService.findByEmail(email));

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("email") String email,
                                              @RequestBody @Valid UserDTO userDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!email.equals(auth.getName()) && !isAdmin(auth))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO updatedUser = converterUtil.convertToUserDTO(
                userService.update(email, converterUtil.convertToUser(userDTO))
        );

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{email}/change-pass")
    public ResponseEntity<MessageResponse> changePassword(@PathVariable("email") String email,
                                                          @RequestBody @Valid ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!email.equals(auth.getName()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(new MessageResponse("You can change password only for your email"));

        userService.changePassword(email, request.getOldPassword(), request.getNewPassword());

        return ResponseEntity.ok(new MessageResponse("Password was successfully updated"));
    }

    @PutMapping("/{email}/change-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> changeUserRole(@PathVariable("email") String email,
                                                          @RequestBody @Valid ChangeRoleRequest request) {
        userService.changeRole(email, request.getRole());
        return ResponseEntity.ok(new MessageResponse("Role was updated"));
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities()
                   .stream()
                   .anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.name()));
    }
}
