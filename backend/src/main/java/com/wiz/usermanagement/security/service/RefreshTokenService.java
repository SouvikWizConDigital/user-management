package com.wiz.usermanagement.security.service;

import com.wiz.usermanagement.model.User;
import com.wiz.usermanagement.security.model.RefreshToken;
import com.wiz.usermanagement.security.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final long refreshTokenDurationMs;

    // 2. Create the constructor manually
    public RefreshTokenService(RefreshTokenRepository repository, @Value("${security.jwt.refresh-token-expiration}") long refreshTokenDurationMs) {
        this.repository = repository;
        this.refreshTokenDurationMs = refreshTokenDurationMs;
    }

    public RefreshToken createRefreshToken(User user) {
        repository.deleteByUser(user);

        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return repository.save(token);
    }

    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        repository.delete(oldToken);
        return createRefreshToken(oldToken.getUser());
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }
}
