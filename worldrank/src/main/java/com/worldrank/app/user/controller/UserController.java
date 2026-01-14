package com.worldrank.app.user.controller;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
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

    @GetMapping("/me/{userId}")
    public ResponseEntity<?> me(@PathVariable UUID userId) {
        logger.info("ingresando a me ::: ");

        
        return new ResponseEntity<>(
            profileService.getMyProfile(userId),
            org.springframework.http.HttpStatus.OK
            );
    }
}
