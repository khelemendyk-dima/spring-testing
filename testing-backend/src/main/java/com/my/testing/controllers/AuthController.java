package com.my.testing.controllers;

import com.my.testing.models.PasswordResetToken;
import com.my.testing.models.RefreshToken;
import com.my.testing.models.User;
import com.my.testing.payload.requests.*;
import com.my.testing.payload.responses.AuthResponse;
import com.my.testing.payload.responses.JwtResponse;
import com.my.testing.payload.responses.MessageResponse;
import com.my.testing.services.PasswordResetTokenService;
import com.my.testing.services.RefreshTokenService;
import com.my.testing.services.UserService;
import com.my.testing.utils.ConverterUtil;
import com.my.testing.utils.EmailSenderUtil;
import com.my.testing.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.my.testing.utils.constants.Email.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenService passwordResetService;
    private final ConverterUtil converterUtil;
    private final JwtUtil jwtUtil;
    private final EmailSenderUtil emailSender;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(jwtUtil.generateJwtToken(user))
                .refreshToken(refreshToken.getToken())
                .user(converterUtil.convertToUserDTO(user))
                .build());
    }

    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        User registeredUser = userService.save(converterUtil.convertToUser(registerRequest));

        new Thread(() -> emailSender.sendEmail(registerRequest.getEmail(), SUBJECT_GREETINGS,
                String.format(MESSAGE_GREETINGS, registerRequest.getFirstName()))).start();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(registeredUser.getId());

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(jwtUtil.generateJwtToken(registeredUser))
                .refreshToken(refreshToken.getToken())
                .user(converterUtil.convertToUserDTO(registeredUser))
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();

        String accessToken = jwtUtil.generateTokenFromEmail(user.getEmail());

        return ResponseEntity.ok(new JwtResponse(accessToken, requestRefreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logOutUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful"));
    }

    @PostMapping("/forgot-pass")
    public ResponseEntity<MessageResponse> sendResetPassTokenToEmail(@RequestBody @Valid ForgotPassRequest request) {
        User user = userService.findByEmail(request.getEmail());

        PasswordResetToken resetToken = passwordResetService.createResetToken(user);

        new Thread(() -> emailSender.sendEmail(user.getEmail(), SUBJECT_NOTIFICATION,
                String.format(MESSAGE_RESET_PASSWORD, user.getFirstName(), resetToken.getToken()))).start();

        return ResponseEntity.ok(new MessageResponse("A reset password link has sent to your email. Please check."));
    }

    @PostMapping("/reset-pass")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody @Valid ResetPassRequest request) {
        PasswordResetToken resetToken = passwordResetService.findByToken(request.getToken());
        passwordResetService.verifyExpiration(resetToken);

        userService.changePassword(resetToken.getUser(), request.getPassword());

        passwordResetService.deleteById(resetToken.getId());

        return ResponseEntity.ok(new MessageResponse("Password was updated"));
    }
}
