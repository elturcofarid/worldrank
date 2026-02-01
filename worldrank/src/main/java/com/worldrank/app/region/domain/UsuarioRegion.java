package com.worldrank.app.region.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.worldrank.app.user.domain.Usuario;

@Entity
@Table(name = "usuario_region")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UsuarioRegion {

    @Id
    @GeneratedValue
    @Column(name = "id_usuario_region")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    @Column(name = "primera_visita", nullable = false)
    private LocalDateTime primera_visita;

    @Column(name = "puntos_obtenidos", nullable = false)
    private int puntosObtenidos;

    @Builder(builderMethodName = "builder")
    public UsuarioRegion(UUID id, Usuario usuario, Region region, LocalDateTime primera_visita, int puntosObtenidos) {
        this.id = id;
        this.usuario = usuario;
        this.region = region;
        this.primera_visita = primera_visita;
        this.puntosObtenidos = puntosObtenidos;
    }
}
