package com.worldrank.app.score.repository;

import com.worldrank.app.score.domain.Visita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VisitaRepository extends JpaRepository<Visita, UUID> {
    List<Visita> findByIdUsuarioAndLugar_Id(UUID idUsuario, UUID idLugar);
    List<Visita> findByIdUsuario(UUID idUsuario);
}

