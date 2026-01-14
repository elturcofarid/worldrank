package com.worldrank.app.publicacion.controller;

import java.util.UUID;

public record CrearPublicacionRequest(
    String descripcion,
    Double longitud,
    Double latitud,
    String imagenBase64
) {
    public String getDescripcion() {
        return descripcion;
    }

    public Double getLongitud() {
        return longitud;
    }

    public Double getLatitud() {
        return latitud;
    }
}