package com.worldrank.app.auth.controller;

import com.worldrank.app.auth.security.JwtProvider;
import com.worldrank.app.auth.service.AuthService;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequest request) {
        authService.register(
            request.email(),
            request.username(),
            request.password(),
            request.country()
        );
    }


 @PostMapping("/login")
public LoginResponse login(@RequestBody LoginRequest request) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.email(),
            request.password()
        )
    );

    String token = jwtProvider.generateToken(authentication.getName());
    return new LoginResponse(token);
}

}
record RegisterRequest(String email, String username, String password, String country) {}