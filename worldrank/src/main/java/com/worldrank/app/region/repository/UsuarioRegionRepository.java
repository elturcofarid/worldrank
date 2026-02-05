package com.worldrank.app.region.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.worldrank.app.region.domain.Region;
import com.worldrank.app.region.domain.UsuarioRegion;
import com.worldrank.app.user.domain.Usuario;

@Repository
public interface UsuarioRegionRepository extends JpaRepository<UsuarioRegion, UUID> {

    /**
     * Verificar si un usuario ya visitó una región
     */
    boolean existsByUsuarioAndRegion(Usuario usuario, Region region);

    /**
     * Buscar registro de visita de usuario a región
     */
    Optional<UsuarioRegion> findByUsuarioAndRegion(Usuario usuario, Region region);

    /**
     * Buscar si un usuario ya visitó un país específico (por nombre)
     */
    boolean existsByUsuarioAndRegionNombreAndRegionTipo(Usuario usuario, String nombreRegion, String tipoRegion);

    /**
     * Buscar registro de país visitado por usuario
     */
    Optional<UsuarioRegion> findByUsuarioAndRegionNombreAndRegionTipo(Usuario usuario, String nombreRegion, String tipoRegion);

    /**
     * Contar países únicos visitados por un usuario
     */
    long countByUsuarioAndRegionTipo(Usuario usuario, String tipoRegion);

    /**
     * Contar ciudades únicas visitadas por un usuario
     */
    long countByUsuarioAndRegionTipoAndRegionPaisIsNotNull(Usuario usuario, String tipoRegion);
}
