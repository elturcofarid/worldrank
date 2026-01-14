package com.worldrank.app.lugar.service;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public Lugar crearLugar(String nombre, String tipoLugar, int puntajeBase, Point point) {

        Lugar lugar = Lugar.builder()
            .id(UUID.randomUUID())
            .nombre(nombre)
            .tipoLugar(tipoLugar)
            .puntajeBase(puntajeBase)
            .geom(point)
            .radioMetros(30)
            .cantidadVisitas(0)
            .autoGenerado(false)
            .fechaCreacion(LocalDateTime.now())
            .confianza(100)
            .build();

        return lugarRepository.save(lugar);
    }

    public Lugar obtenerLugarCercano(Point point) {

     return lugarRepository
            .findLugarCercano(point,100)
            .orElseGet(() -> crearNuevoLugar(point));
    }


    private Lugar crearNuevoLugar(Point point) {
        Lugar lugar = Lugar.builder()
            .geom(point)
            .radioMetros(30)
            .cantidadVisitas(0)
            .autoGenerado(true)
            .fechaCreacion(LocalDateTime.now())
            .confianza(10)
            .build();

        return lugarRepository.save(lugar);
    }

}
