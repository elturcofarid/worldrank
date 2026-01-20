package com.worldrank.app.publicacion.dto;

import java.util.UUID;

public class UsuarioSummary {
    private UUID id;
    private String nombre;
    private String avatarUrl;

    public UsuarioSummary() {}

    public UsuarioSummary(UUID id, String nombre, String avatarUrl) {
        this.id = id;
        this.nombre = nombre;
        this.avatarUrl = avatarUrl;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}