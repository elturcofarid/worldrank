package com.worldrank.app.publicacion.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PublicacionListResponse {
    private UUID id;
    private String descripcion;
    private String imagenUrl;
    private Double longitud;
    private Double latitud;
    private OffsetDateTime fechaCreacion;
    private UsuarioSummary usuario;

    public PublicacionListResponse() {}

    public PublicacionListResponse(UUID id, String descripcion, String imagenUrl, Double longitud, Double latitud, OffsetDateTime fechaCreacion, UsuarioSummary usuario) {
        this.id = id;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.longitud = longitud;
        this.latitud = latitud;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public OffsetDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(OffsetDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public UsuarioSummary getUsuario() { return usuario; }
    public void setUsuario(UsuarioSummary usuario) { this.usuario = usuario; }
}