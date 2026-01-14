package com.worldrank.app.visita.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.worldrank.app.lugar.domain.Lugar;
import com.worldrank.app.user.domain.Usuario;

@Entity
@Table(
    name = "visita",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_lugar"})
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Visita {

    @Id
    @Column(name = "id_visita")
    private UUID id;

    /* 
    @Column(name = "id_usuario", nullable = false)
    private UUID idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lugar", nullable = false)
    private Lugar lugar;
    */

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lugar_id", nullable = false)
    private Lugar lugar;

    @Column(name = "fecha_visita")
   // private OffsetDateTime fechaVisita = OffsetDateTime.now();
    private LocalDateTime fechaVisita = LocalDateTime.now();

    @Column(name = "puntos_obtenidos", nullable = false)
    private int puntosObtenidos;

    
}
