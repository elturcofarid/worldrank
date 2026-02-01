package com.worldrank.app.region.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.worldrank.app.region.domain.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, UUID> {

    /**
     * Buscar país por nombre
     */
    Optional<Region> findByNombreAndTipo(String nombre, String tipo);

    /**
     * Buscar país por código ISO
     */
    Optional<Region> findByCodigoIsoAndTipo(String codigoIso, String tipo);

    /**
     * Buscar ciudad por nombre y país
     */
    Optional<Region> findByNombreAndPais(String nombre, Region pais);

    /**
     * Verificar si existe un país por nombre
     */
    boolean existsByNombreAndTipo(String nombre, String tipo);

    /**
     * Verificar si existe una ciudad por nombre y país
     */
    boolean existsByNombreAndPais(String nombre, Region pais);
}
