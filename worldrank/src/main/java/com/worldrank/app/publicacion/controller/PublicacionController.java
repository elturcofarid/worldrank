package com.worldrank.app.publicacion.controller;

import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.worldrank.app.publicacion.service.PublicacionService;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE ,
                  MediaType.APPLICATION_OCTET_STREAM_VALUE
    }
    )
    public ResponseEntity<PublicacionResponse> crear(
            @RequestPart("imagen") MultipartFile imagen,
            @RequestPart("request") CrearPublicacionRequest request)
            //,            @AuthenticationPrincipal Jwt jwt) 
            {

        //UUID idUsuario = UUID.fromString(jwt.getSubject());
        UUID idUsuario = UUID.randomUUID();
        //prueba hay que obtener el usuario desde el jwt

        return ResponseEntity.ok(
                publicacionService.crearPublicacion(idUsuario, imagen, request)
        );
    }
}
