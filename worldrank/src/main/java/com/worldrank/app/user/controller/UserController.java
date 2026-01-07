package com.worldrank.app.user.controller;

import com.worldrank.app.user.repository.ProfileRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private ProfileRepository profileRepository;

    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal Object principal) {
        return profileRepository.findAll(); // simplificado MVP
    }
}
