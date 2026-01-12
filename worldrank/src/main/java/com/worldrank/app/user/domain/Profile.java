package com.worldrank.app.user.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import com.worldrank.app.ranking.domain.Rank;

@Entity
@Table(name = "perfil")
@Getter @Setter
@NoArgsConstructor
public class Profile {

    public Profile(UUID id, User user, String biografia, Integer score) {
        this.id = id;
        this.user = user;
        this.biografia = biografia;
        this.score = score;
    }

    @Id
    @Column(name = "id_perfil")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    private String biografia;

    @Column(name = "puntaje_total")
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rango")
    private Rank rango;

    @Column(name = "foto_perfil")
    private String fotoUrl;

    public Integer getScore() {
        return score;
    }

    public Rank getRango() {
        return rango;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }
}