package com.worldrank.app.lugar.service;

import java.util.UUID;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import com.worldrank.app.lugar.domain.Lugar;
import com.worldrank.app.lugar.repository.LugarRepository;

@Service
public class LugarService {

    private final LugarRepository lugarRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public LugarService(LugarRepository lugarRepository) {
        this.lugarRepository = lugarRepository;
    }

    public Lugar obtenerPorId(UUID idLugar) {
        return lugarRepository.findById(idLugar)
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
    }

    public Lugar crearLugar(String nombre, String tipoLugar, int puntajeBase, double longitud, double latitud) {
        Lugar lugar = new Lugar();
        lugar.setId(UUID.randomUUID());
        lugar.setNombre(nombre);
        lugar.setTipoLugar(tipoLugar);
        lugar.setPuntajeBase(puntajeBase);
        Point point = geometryFactory.createPoint(new Coordinate(longitud, latitud));
        point.setSRID(4326);
        lugar.setGeom(point);
        return lugarRepository.save(lugar);
    }
}
