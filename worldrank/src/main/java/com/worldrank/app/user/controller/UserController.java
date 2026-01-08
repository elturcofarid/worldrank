package com.worldrank.app.user.controller;

import com.worldrank.app.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;

import java.util.logging.Logger;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private ProfileRepository profileRepository;
    private Logger logger = Logger.getLogger(UserController.class.getName());

    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal Object principal) {
        logger.info("ingresando a me ::: ");
        return profileRepository.findAll(); // simplificado MVP
    }
}
