package com.worldrank.app.post.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "publicacion")
@Data
@NoArgsConstructor
public class Post {

    public Post(UUID id, UUID userId, UUID placeId, String descripcion, String imageUrl, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.placeId = placeId;
        this.descripcion = descripcion;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    @Id
    @Column(name = "id_publicacion")
    private UUID id;

    @Column(name = "id_usuario", nullable = false)
    private UUID userId;

    @Column(name = "id_lugar", nullable = false)
    private UUID placeId;

    private String descripcion;

    @Column(name = "url_imagen", nullable = false)
    private String imageUrl;

    @Column(name = "fecha_publicacion")
    private OffsetDateTime createdAt;

    public UUID getId() {
        return id;
    }
}
