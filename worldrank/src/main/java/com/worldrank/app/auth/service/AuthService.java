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

    public AuthService(UserRepository userRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
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
}