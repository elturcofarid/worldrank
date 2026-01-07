package com.worldrank.app.score.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "visita")
@Getter @Setter
@NoArgsConstructor

public class Visit {

    public Visit(UUID id, UUID userId, UUID placeId, OffsetDateTime visitedAt) {
        this.id = id;
        this.userId = userId;
        this.placeId = placeId;
        this.visitedAt = visitedAt;
    }


    @Id
    @Column(name = "id_visita")
    private UUID id;

    @Column(name = "id_usuario", nullable = false)
    private UUID userId;

    @Column(name = "id_lugar", nullable = false)
    private UUID placeId;

    @Column(name = "fecha_visita")
    private OffsetDateTime visitedAt;
}
