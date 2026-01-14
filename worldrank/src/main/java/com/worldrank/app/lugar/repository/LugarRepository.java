package com.worldrank.app.lugar.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.locationtech.jts.geom.Point;
import com.worldrank.app.lugar.domain.Lugar;

public interface LugarRepository
        extends JpaRepository<Lugar, UUID> {

    @Query(value = """
        SELECT *
        FROM lugar l
        WHERE ST_DWithin(
            l.geom,
            :point,
            :radio
        )
        ORDER BY ST_Distance(l.geom, :point)
        LIMIT 1
        """,
        nativeQuery = true)
    Optional<Lugar> findLugarCercano(
        @Param("point") Point point,
        @Param("radio") double radio
    );
}