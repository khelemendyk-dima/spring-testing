package com.my.testing.services.imp;

import com.my.testing.exceptions.RefreshTokenExpiredException;
import com.my.testing.exceptions.RefreshTokenNotFoundException;
import com.my.testing.models.RefreshToken;
import com.my.testing.repositories.RefreshTokenRepository;
import com.my.testing.services.RefreshTokenService;
import com.my.testing.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${jwt.refresh.expirationMs}")
    private Long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() ->
                new RefreshTokenNotFoundException(String.format("Refresh token '%s' not found. Please make a new login request", token))
        );
    }

    @Override
    public RefreshToken findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId).orElseThrow(() ->
                new RefreshTokenNotFoundException(String.format("Refresh token with user id '%s' not found. Please make a new login request", userId)));
    }

    @Transactional
    @Override
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userService.findById(userId));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        Optional<RefreshToken> tokenFromDB = refreshTokenRepository.findByUserId(userId);

        tokenFromDB.ifPresent(token -> refreshToken.setId(token.getId()));

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    @Override
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUserId(userId);
    }

    @Transactional
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException(String.format("Refresh token '%s' was expired. Please make a new login request", token.getToken()));
        }

        return token;
    }


}
