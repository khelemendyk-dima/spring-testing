package com.my.testing.services;


import com.my.testing.models.RefreshToken;

public interface RefreshTokenService {
    RefreshToken findByToken(String token);
    RefreshToken findByUserId(Long userId);
    RefreshToken createRefreshToken(Long userId);
    int deleteByUserId(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
}
