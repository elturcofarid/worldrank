package com.worldrank.app.auth.service;

import com.worldrank.app.user.domain.*;
import com.worldrank.app.user.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private PasswordEncoder passwordEncoder;
    private com.worldrank.app.auth.security.JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder, com.worldrank.app.auth.security.JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public void register(String email, String username, String password, String country) {
        User user = new User(
                UUID.randomUUID(),
                email,
                username,
                passwordEncoder.encode(password),
                country
        );
        userRepository.save(user);

        Profile profile = new Profile(
                UUID.randomUUID(),
                user,
                null,
                1
        );
        profileRepository.save(profile);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        // Generate JWT with user and profile data
        return jwtProvider.generateToken(user.getId().toString(), profile.getId().toString());
    }
}