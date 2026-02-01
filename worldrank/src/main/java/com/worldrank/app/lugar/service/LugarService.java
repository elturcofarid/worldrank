package com.worldrank.app.lugar.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import com.worldrank.app.geocoding.PlaceInfo;
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

    /**
     * Obtiene un lugar cercano, actualizándolo con datos de geocoding si es necesario
     */
    public Lugar obtenerLugarCercano(Point point, PlaceInfo placeInfo) {
        Optional<Lugar> lugarExistente = lugarRepository.findLugarCercano(point, 100);
        
        if (lugarExistente.isPresent()) {
            Lugar lugar = lugarExistente.get();
            
            // Si el lugar no tiene nombre o tiene baja confianza, actualizarlo
            if (debeActualizarNombre(lugar, placeInfo)) {
                actualizarLugarConGeocoding(lugar, placeInfo);
                return lugarRepository.save(lugar);
            }
            return lugar;
        }
        
        // Crear nuevo lugar
        return crearNuevoLugar(point, placeInfo);
    }

    /**
     * Versión original para compatibilidad - usa coordenadas como nombre
     */
    public Lugar obtenerLugarCercano(Point point) {
        return obtenerLugarCercano(point, null);
    }

    /**
     * Determina si el lugar debe ser actualizado con datos del geocoding
     */
    private boolean debeActualizarNombre(Lugar lugar, PlaceInfo placeInfo) {
        if (placeInfo == null || !placeInfo.esLugarConocido()) {
            return false;
        }
        
        // Actualizar si:
        // 1. El lugar no tiene nombre (auto-generado)
        // 2. El lugar tiene nombre basado en coordenadas
        // 3. El lugar tiene baja confianza (< 50)
        // 4. El nuevo dato tiene mayor confianza
        boolean nombreInvalido = lugar.getNombre() == null || 
                                   lugar.getNombre().startsWith("Lat:") ||
                                   lugar.getNombre().equals("Ubicación desconocida");
        
        return nombreInvalido && lugar.getConfianza() < 80;
    }

    /**
     * Actualiza un lugar existente con datos del geocoding
     */
    private void actualizarLugarConGeocoding(Lugar lugar, PlaceInfo placeInfo) {
        lugar.setNombre(placeInfo.name());
        if (placeInfo.category() != null && (lugar.getTipoLugar() == null || lugar.getTipoLugar().equals("desconocido"))) {
            lugar.setTipoLugar(placeInfo.category());
        }
        // Actualizar país y ciudad si no existen
        if (lugar.getPais() == null && placeInfo.country() != null) {
            lugar.setPais(placeInfo.country());
        }
        if (lugar.getCiudad() == null && placeInfo.city() != null) {
            lugar.setCiudad(placeInfo.city());
        }
        lugar.setConfianza(calcularConfianza(placeInfo));
        // El lugar deja de ser auto-generado si tiene nombre real
        if (placeInfo.esLugarConocido()) {
            lugar.setAutoGenerado(false);
        }
    }

    /**
     * Crea un nuevo lugar con datos de geocoding o nombre genérico
     */
    private Lugar crearNuevoLugar(Point point, PlaceInfo placeInfo) {
        String nombre;
        String tipoLugar;
        int confianza;
        boolean autoGenerado;

        if (placeInfo != null && placeInfo.esLugarConocido()) {
            // Usar datos del geocoding
            nombre = placeInfo.name();
            tipoLugar = placeInfo.category();
            confianza = calcularConfianza(placeInfo);
            autoGenerado = false;
        } else {
            // Generar nombre basado en coordenadas
            nombre = generarNombreCoordenadas(point);
            tipoLugar = placeInfo != null && placeInfo.category() != null ? 
                        placeInfo.category() : "desconocido";
            confianza = 10;
            autoGenerado = true;
        }

        Lugar lugar = Lugar.builder()
            .id(UUID.randomUUID())
            .nombre(nombre)
            .tipoLugar(tipoLugar)
            .pais(placeInfo != null ? placeInfo.country() : null)
            .ciudad(placeInfo != null ? placeInfo.city() : null)
            .puntajeBase(100)
            .geom(point)
            .radioMetros(30)
            .cantidadVisitas(0)
            .autoGenerado(autoGenerado)
            .fechaCreacion(LocalDateTime.now())
            .confianza(confianza)
            .build();

        return lugarRepository.save(lugar);
    }

    /**
     * Genera un nombre basado en coordenadas cuando no hay geocoding
     */
    private String generarNombreCoordenadas(Point point) {
        double lat = Math.round(point.getY() * 10000.0) / 10000.0;
        double lng = Math.round(point.getX() * 10000.0) / 10000.0;
        return String.format("Lat: %.4f, Lng: %.4f", lat, lng);
    }

    /**
     * Calcula la confianza basada en la calidad del geocoding
     */
    private int calcularConfianza(PlaceInfo placeInfo) {
        if (placeInfo == null) return 10;
        
        // Mapbox devuelve diferentes tipos con diferentes confiabilidades
        String category = placeInfo.category();
        if (category == null) return 40;
        
        return switch (category.toLowerCase()) {
            case "poi" -> 80;           // Punto de interés conocido
            case "address" -> 70;       // Dirección específica
            case "locality" -> 60;      // Barrio/ciudad
            case "place" -> 50;         // Ciudad/población
            case "region" -> 30;        // Región/estado
            case "country" -> 20;       // País
            default -> 40;
        };
    }

}
