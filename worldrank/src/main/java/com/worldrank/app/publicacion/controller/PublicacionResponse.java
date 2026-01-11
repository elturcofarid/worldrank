package com.worldrank.app.publicacion.controller;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PublicacionResponse (
    UUID idPublicacion,
    OffsetDateTime fechaPublicacion,
    int puntajeOtorgado,
    boolean lugarNuevo
) {
}