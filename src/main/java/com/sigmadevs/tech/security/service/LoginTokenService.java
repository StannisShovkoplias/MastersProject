package com.sigmadevs.tech.security.service;

import com.sigmadevs.tech.app.entity.User;
import com.sigmadevs.tech.security.entity.LoginToken;
import com.sigmadevs.tech.security.entity.Token;
import com.sigmadevs.tech.security.repository.LoginTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginTokenService {
    private final LoginTokenRepository confirmationTokenRepository;

    public LoginToken generateConfirmationToken(User user) {
        LoginToken token = new LoginToken(null, UUID.randomUUID().toString(), user, LocalDateTime.now().plusHours(24), Token.CONFIRM);
        log.info("Generating confirmation token for {}", user.getUsername());
        return confirmationTokenRepository.save(token);
    }

    public Optional<LoginToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public LoginToken generateResetToken(User user) {
        LoginToken token = new LoginToken(null, UUID.randomUUID().toString(), user, LocalDateTime.now().plusHours(24), Token.RESET);
        log.info("Generating reset token for {}", user.getUsername());
        return confirmationTokenRepository.save(token);
    }

    public void delete(LoginToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }
}
