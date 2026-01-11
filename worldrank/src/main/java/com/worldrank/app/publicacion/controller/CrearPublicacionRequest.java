package com.worldrank.app.publicacion.controller;

import java.util.UUID;

@lombok.Data
public class CrearPublicacionRequest {

    private UUID idLugar;
    private String descripcion;
    private Double latitud;
    private Double longitud;

    // getters y setters
}