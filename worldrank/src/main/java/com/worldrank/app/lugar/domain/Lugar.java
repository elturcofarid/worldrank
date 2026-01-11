package com.worldrank.app.lugar.domain;

import java.util.UUID;

import org.locationtech.jts.geom.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lugar")
public class Lugar {

    @Id
    @GeneratedValue
    @Column(name = "id_lugar")
    private UUID id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "tipo_lugar")
    private String tipoLugar;

    @Column(name = "puntaje_base", nullable = false)
    private int puntajeBase;

    @Column(columnDefinition = "geography(Point,4326)")
    private Point geom;

    // ========================
    // getters y setters
    // ========================

    public UUID getId() {
        return id;
    }

    public int getPuntajeBase() {
        return puntajeBase;
    }
}
