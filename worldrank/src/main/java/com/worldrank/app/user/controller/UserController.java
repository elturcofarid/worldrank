package com.worldrank.app.user.controller;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.worldrank.app.user.service.ProfileService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class.getName());
    private ProfileService profileService;

    public UserController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Jwt jwt) {
        logger.info("ingresando a me ::: ");

        UUID idUsuario;
        if (jwt != null) {
            String userId = jwt.getSubject();
            idUsuario = UUID.fromString(userId);
        } else {
            // Use a default user for anonymous access
            idUsuario = UUID.fromString("00000000-0000-0000-0000-000000000000");
        }
        
        return new ResponseEntity<>(
            profileService.getMyProfile(idUsuario),
            org.springframework.http.HttpStatus.OK
            );
    }
}
