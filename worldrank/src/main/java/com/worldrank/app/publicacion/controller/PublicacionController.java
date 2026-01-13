package com.worldrank.app.publicacion.controller;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.worldrank.app.publicacion.service.PublicacionService;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    private static final Logger logger = LoggerFactory.getLogger(PublicacionController.class);

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping
    public ResponseEntity<PublicacionResponse> crear(
            @RequestBody CrearPublicacionRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        logger.info("Received crear request: {}", request);

        UUID idUsuario = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(
                publicacionService.crearPublicacion(idUsuario, request)
        );
    }
}
