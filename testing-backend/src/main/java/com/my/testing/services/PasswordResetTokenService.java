package com.my.testing.services;

import com.my.testing.models.PasswordResetToken;
import com.my.testing.models.User;

public interface PasswordResetTokenService {
    PasswordResetToken findByToken(String token);
    PasswordResetToken createResetToken(User user);
    void deleteById(Long tokenId);

    void verifyExpiration(PasswordResetToken resetToken);
}
