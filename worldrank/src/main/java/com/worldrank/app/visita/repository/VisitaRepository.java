package com.worldrank.app.visita.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldrank.app.lugar.domain.Lugar;
import com.worldrank.app.user.domain.Usuario;
import com.worldrank.app.visita.domain.Visita;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitaRepository extends JpaRepository<Visita, UUID> {


        List<Visita> findByUsuarioAndLugar( Usuario usuario,
        Lugar lugar);

    List<Visita> findByUsuario(Usuario usuario);

    /* 
     Optional<Visita> findTopByUserIdAndLugarOrderByFechaVisitaDesc(
        Usuario usuario,
        Lugar lugar
    );
    */
         Optional<Visita> findTopByUsuarioAndLugarOrderByFechaVisitaDesc(
        Usuario usuario,
        Lugar lugar
    );

    /*
    boolean existsByUserIdAndLugarAndFechaVisitaAfter(
        Usuario usuario,
        Lugar lugar,
        LocalDateTime fecha
    );
     */

        boolean existsByUsuarioAndLugarAndFechaVisitaAfter(
        Usuario usuario,
        Lugar lugar,
        LocalDateTime fecha
    );
}

