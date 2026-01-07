package com.worldrank.app.auth.security;

import com.worldrank.app.user.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructor EXPLÍCITO (evita problemas de Lombok / inyección)
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        var user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + email)
                );

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
