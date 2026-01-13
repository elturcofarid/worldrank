package com.worldrank.app.score.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.worldrank.app.lugar.domain.Lugar;

@Entity
@Table(
    name = "visita",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_lugar"})
)
@lombok.Data
public class Visita {

    @Id
    @Column(name = "id_visita")
    private UUID id;

    @Column(name = "id_usuario", nullable = false)
    private UUID idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lugar", nullable = false)
    private Lugar lugar;

    @Column(name = "fecha_visita")
    private OffsetDateTime fechaVisita = OffsetDateTime.now();

    @Column(name = "puntaje_otorgado", nullable = false)
    private int puntajeOtorgado;

    public void setIdUsuario(UUID idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public void setPuntajeOtorgado(int puntajeOtorgado) {
        this.puntajeOtorgado = puntajeOtorgado;
    }

    public Lugar getLugar() {
        return lugar;
    }
}
