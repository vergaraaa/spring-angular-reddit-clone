package com.vergaraaa.backend.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vergaraaa.backend.controller.RegisterRequest;
import com.vergaraaa.backend.exceptions.SpringRedditException;
import com.vergaraaa.backend.model.NotificationEmail;
import com.vergaraaa.backend.model.User;
import com.vergaraaa.backend.model.VerificationToken;
import com.vergaraaa.backend.repository.UserRepository;
import com.vergaraaa.backend.repository.VerificationTokenRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail(
                "Please activate your account",
                user.getEmail(),
                "Thank you for signing up to Spring Reddit, "
                        + " please click on the link below to activate your account: "
                        + "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifiyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid token"));

        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("User not found with name " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }
}
