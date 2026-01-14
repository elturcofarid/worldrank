package com.worldrank.app.publicacion.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.worldrank.app.publicacion.domain.Publicacion;

import java.util.UUID;

public interface PublicacionRepository extends JpaRepository<Publicacion, UUID> {
    Page<Publicacion> findAllByOrderByFechaPublicacionDesc(Pageable pageable);
}
