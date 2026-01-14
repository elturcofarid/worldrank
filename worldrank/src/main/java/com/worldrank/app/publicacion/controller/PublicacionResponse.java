package com.worldrank.app.publicacion.controller;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PublicacionResponse(
    UUID id,
    UUID idUsuario,
    UUID idLugar,
    String descripcion,
    String urlImagen,
    OffsetDateTime fechaPublicacion,
    int puntaje,
    boolean lugarNuevo
) {
}