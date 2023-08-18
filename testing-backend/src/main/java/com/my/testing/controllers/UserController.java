package com.my.testing.controllers;

import com.my.testing.dtos.UserDTO;
import com.my.testing.models.enums.Role;
import com.my.testing.payload.requests.ChangePasswordRequest;
import com.my.testing.payload.requests.ChangeRoleRequest;
import com.my.testing.payload.responses.ErrorResponse;
import com.my.testing.payload.responses.MessageResponse;
import com.my.testing.services.UserService;
import com.my.testing.utils.ConverterUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * UserController class. Used for interacting with user.
 *
 * @author Khelemendyk Dmytro
 * @version 1.0
 */

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ConverterUtil converterUtil;

    @Operation(summary = "Gets user by email",
               description = "Method compares logged email to input parameter, also checks logged user's role. " +
                       "Admin can receive info about each user by email, but user can get info only about himself. " +
                       "Returns user's info",
               parameters = {@Parameter(name = "email", description = "User's email", example = "example@gmail.com")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Full authentication is required to access this resource", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You don't have access to this info", content = @Content),
            @ApiResponse(responseCode = "404", description = "User with this email wasn't found",
                         content = @Content(schema = @Schema(name = "Error response", implementation = ErrorResponse.class)))})
    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable("email") String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!email.equals(auth.getName()) && !isAdmin(auth))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        UserDTO userDTO = converterUtil.convertToUserDTO(userService.findByEmail(email));

        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Updates user's info",
               description = "Method compares logged email to input parameter, also checks logged user's role. " +
                       "Admin can change info about each user, but user can change only his info",
               parameters = {@Parameter(name = "email", description = "User to be updated", example = "example@gmail.com")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Full authentication is required to access this resource", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You don't have access to this info", content = @Content),
            @ApiResponse(responseCode = "404", description = "User with this email wasn't found",
                    content = @Content(schema = @Schema(name = "Error response", implementation = ErrorResponse.class)))})
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

    @Operation(summary = "Changes user's old password with a new one",
               description = "User can change only his password. Method compares logged email to input parameter. " +
                       "Before changing password compares old password from db with the input one.",
               parameters = {@Parameter(name = "email", description = "User's email", example = "example@gmail.com")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password was successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Full authentication is required to access this resource", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. You can change password only for your email", content = @Content),
            @ApiResponse(responseCode = "409", description = "Old password doesn't match.",
                    content = @Content(schema = @Schema(name = "Error response", implementation = ErrorResponse.class)))})
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

    @Operation(summary = "Changes user's role",
               description = "Method is allowed only for admins! Changes user's role with a new one.",
               parameters = {@Parameter(name = "email", description = "User's email", example = "example@gmail.com")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role was updated"),
            @ApiResponse(responseCode = "400", description = "Bad request. You most likely specified the wrong role or email.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Full authentication is required to access this resource", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden. Only admins can change user's role", content = @Content)})
    @PutMapping("/{email}/change-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> changeUserRole(@PathVariable("email") String email,
                                                          @RequestBody @Valid ChangeRoleRequest request) {
        userService.changeRole(email, request.getRole());
        return ResponseEntity.ok(new MessageResponse("Role was updated"));
    }


    /**
     * Checks if user is admin
     * @param auth authenticated user
     * @return true if user is admin, else - false.
     */
    public boolean isAdmin(Authentication auth) {
        return auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.name()));
    }
}
