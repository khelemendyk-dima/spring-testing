package com.my.testing.controllers;

import com.my.testing.dtos.requests.LoginRequest;
import com.my.testing.dtos.requests.RefreshTokenRequest;
import com.my.testing.dtos.requests.RegisterRequest;
import com.my.testing.dtos.responses.JwtResponse;
import com.my.testing.dtos.responses.MessageResponse;
import com.my.testing.models.RefreshToken;
import com.my.testing.models.User;
import com.my.testing.services.RefreshTokenService;
import com.my.testing.services.UserService;
import com.my.testing.utils.ConverterUtil;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final ConverterUtil converterUtil;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtUtil.generateJwtToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/registration")
    public ResponseEntity<MessageResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        userService.save(converterUtil.convertToUser(registerRequest));
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
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
}
