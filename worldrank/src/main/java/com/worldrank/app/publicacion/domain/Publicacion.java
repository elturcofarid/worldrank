package com.worldrank.app.publicacion.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.locationtech.jts.geom.Point;

import com.worldrank.app.lugar.domain.Lugar;

@Entity
@Table(name = "publicacion")
@lombok.Data
public class Publicacion {

    @Id
    @GeneratedValue
    @Column(name = "id_publicacion")
    private UUID id;

    @Column(name = "id_usuario", nullable = false)
    private UUID idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lugar", nullable = false)
    private Lugar lugar;

    @Column
    private String descripcion;

    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;

    @Column(name = "fecha_publicacion")
    private OffsetDateTime fechaPublicacion = OffsetDateTime.now();

    @Column(columnDefinition = "geography(Point,4326)")
    private Point gps;

    // getters y setters
}
