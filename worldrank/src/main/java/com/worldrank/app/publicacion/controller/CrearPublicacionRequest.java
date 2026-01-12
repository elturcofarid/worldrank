package com.worldrank.app.publicacion.controller;

import java.util.UUID;

public record CrearPublicacionRequest(
    UUID idLugar,
    String descripcion,
    double longitud,
    double latitud,
    String imagenBase64
) {
    public UUID getIdLugar() {
        return idLugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getLatitud() {
        return latitud;
    }
}