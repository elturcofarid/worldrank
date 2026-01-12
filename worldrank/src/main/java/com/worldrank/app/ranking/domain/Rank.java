package com.worldrank.app.ranking.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rango")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rango")
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "puntaje_min", nullable = false)
    private Integer puntajeMin;

    @Column(name = "puntaje_max", nullable = false)
    private Integer puntajeMax;

    public String getNombre() {
        return nombre;
    }
}
