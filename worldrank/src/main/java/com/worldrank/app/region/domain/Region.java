package com.worldrank.app.region.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "region")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {

    @Id
    @GeneratedValue
    @Column(name = "id_region")
    private UUID id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "tipo", nullable = false)
    private String tipo; // "PAIS" o "CIUDAD"

    @Column(name = "codigo_iso")
    private String codigoIso; // Código ISO para países (ES, MX, AR)

    /**
     * Para ciudades: referencia al país
     */
    @ManyToOne
    @JoinColumn(name = "id_pais")
    private Region pais;

    /**
     * Centroide de la región para cálculos geográficos
     */
    @Column(columnDefinition = "geography(Point,4326)")
    private Point centro;

    /**
     * Fecha de creación de la región
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Builder(builderMethodName = "builder")
    public Region(UUID id, String nombre, String tipo, String codigoIso, Region pais, Point centro) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.codigoIso = codigoIso;
        this.pais = pais;
        this.centro = centro;
        this.fechaCreacion = LocalDateTime.now();
    }

    /**
     * Verifica si es un país
     */
    public boolean esPais() {
        return "PAIS".equals(tipo);
    }

    /**
     * Verifica si es una ciudad
     */
    public boolean esCiudad() {
        return "CIUDAD".equals(tipo);
    }
}
