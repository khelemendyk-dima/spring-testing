package com.my.testing.services.imp;

import com.my.testing.exceptions.PasswordResetTokenExpiredException;
import com.my.testing.exceptions.PasswordResetTokenNotFoundException;
import com.my.testing.models.PasswordResetToken;
import com.my.testing.models.User;
import com.my.testing.repositories.PasswordResetTokenRepository;
import com.my.testing.services.PasswordResetTokenService;
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
public class PasswordResetTokenImpl implements PasswordResetTokenService {
    @Value("${password.reset.expirationMs}")
    private Long resetTokenDurationMs;
    private final PasswordResetTokenRepository resetTokenRepository;

    @Override
    public PasswordResetToken findByToken(String token) {
        return resetTokenRepository.findByToken(token).orElseThrow(() ->
                new PasswordResetTokenNotFoundException(String.format("Reset token '%s' not found.", token)));
    }

    @Transactional
    @Override
    public PasswordResetToken createResetToken(User user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();

        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(Instant.now().plusMillis(resetTokenDurationMs));
        passwordResetToken.setToken(UUID.randomUUID().toString());

        Optional<PasswordResetToken> tokenFromDB = resetTokenRepository.findByUserId(user.getId());
        tokenFromDB.ifPresent(token -> passwordResetToken.setId(token.getId()));

        return resetTokenRepository.save(passwordResetToken);
    }

    @Transactional
    @Override
    public void deleteById(Long tokenId) {
        resetTokenRepository.deleteById(tokenId);
    }

    @Transactional
    @Override
    public void verifyExpiration(PasswordResetToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            resetTokenRepository.delete(token);
            throw new PasswordResetTokenExpiredException(String.format("Reset token '%s' was expired. Please make new reset password request.", token.getToken()));
        }
    }

}
