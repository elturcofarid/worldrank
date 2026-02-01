package com.worldrank.app.region.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worldrank.app.geocoding.PlaceInfo;
import com.worldrank.app.region.domain.Region;
import com.worldrank.app.region.domain.ResultadoRegion;
import com.worldrank.app.region.domain.UsuarioRegion;
import com.worldrank.app.region.repository.RegionRepository;
import com.worldrank.app.region.repository.UsuarioRegionRepository;
import com.worldrank.app.user.domain.Usuario;

@Service
@Transactional
public class RegionService {

    private static final int PUNTOS_PAIS_NUEVO = 500;
    private static final int PUNTOS_CIUDAD_NUEVA = 200;

    private final RegionRepository regionRepository;
    private final UsuarioRegionRepository usuarioRegionRepository;

    public RegionService(RegionRepository regionRepository, UsuarioRegionRepository usuarioRegionRepository) {
        this.regionRepository = regionRepository;
        this.usuarioRegionRepository = usuarioRegionRepository;
    }

    /**
     * Obtiene o crea un país desde la información del geocoding
     */
    public Region obtenerOCrearPais(PlaceInfo placeInfo) {
        if (placeInfo == null || placeInfo.country() == null) {
            return null;
        }

        String paisNombre = placeInfo.country();
        String codigoIso = placeInfo.countryCode();

        // Buscar por código ISO primero (más preciso)
        if (codigoIso != null && !codigoIso.isEmpty()) {
            return regionRepository.findByCodigoIsoAndTipo(codigoIso, "PAIS")
                .orElseGet(() -> crearPais(paisNombre, codigoIso));
        }

        // Buscar por nombre
        return regionRepository.findByNombreAndTipo(paisNombre, "PAIS")
            .orElseGet(() -> crearPais(paisNombre, codigoIso));
    }

    /**
     * Crea un nuevo país
     */
    private Region crearPais(String nombre, String codigoIso) {
        Region pais = Region.builder()
            .id(UUID.randomUUID())
            .nombre(nombre)
            .codigoIso(codigoIso)
            .tipo("PAIS")
            .build();
        return regionRepository.save(pais);
    }

    /**
     * Obtiene o crea una ciudad desde la información del geocoding
     */
    public Region obtenerOCrearCiudad(PlaceInfo placeInfo, Region pais) {
        if (placeInfo == null || placeInfo.city() == null) {
            return null;
        }

        String ciudadNombre = placeInfo.city();

        // Si tenemos país, buscar por nombre y país
        if (pais != null) {
            return regionRepository.findByNombreAndPais(ciudadNombre, pais)
                .orElseGet(() -> crearCiudad(ciudadNombre, pais));
        }

        // Si no tenemos país, crear ciudad sin referencia (caso excepcional)
        Region ciudad = Region.builder()
            .id(UUID.randomUUID())
            .nombre(ciudadNombre)
            .tipo("CIUDAD")
            .build();
        return regionRepository.save(ciudad);
    }

    /**
     * Crea una nueva ciudad
     */
    private Region crearCiudad(String nombre, Region pais) {
        Region ciudad = Region.builder()
            .id(UUID.randomUUID())
            .nombre(nombre)
            .pais(pais)
            .tipo("CIUDAD")
            .build();
        return regionRepository.save(ciudad);
    }

    /**
     * Verifica si es la primera vez que un usuario visita un país
     */
    public boolean esPrimeraVisitaPais(Usuario usuario, String paisNombre) {
        if (paisNombre == null || paisNombre.isEmpty()) {
            return false;
        }
        return !usuarioRegionRepository.existsByUsuarioAndRegionNombreAndRegionTipo(
            usuario, paisNombre, "PAIS");
    }

    /**
     * Verifica si es la primera vez que un usuario visita una ciudad
     */
    public boolean esPrimeraVisitaCiudad(Usuario usuario, String ciudadNombre) {
        if (ciudadNombre == null || ciudadNombre.isEmpty()) {
            return false;
        }
        return !usuarioRegionRepository.existsByUsuarioAndRegionNombreAndRegionTipo(
            usuario, ciudadNombre, "CIUDAD");
    }

    /**
     * Registra la primera visita de un usuario a una región y devuelve los puntos obtenidos
     */
    public int registrarPrimeraVisitaregion(Usuario usuario, Region region, boolean esPais) {
        int puntos = esPais ? PUNTOS_PAIS_NUEVO : PUNTOS_CIUDAD_NUEVA;

        // Verificar que no exista ya el registro
        if (usuarioRegionRepository.existsByUsuarioAndRegion(usuario, region)) {
            return 0; // Ya fue registrada la visita
        }

        UsuarioRegion usuarioRegion = UsuarioRegion.builder()
            .id(UUID.randomUUID())
            .usuario(usuario)
            .region(region)
            .primera_visita(LocalDateTime.now())
            .puntosObtenidos(puntos)
            .build();

        usuarioRegionRepository.save(usuarioRegion);
        return puntos;
    }

    /**
     * Procesa la información de geocoding y registra países/ciudades nuevas
     * Retorna los puntos obtenidos por nuevas regiones
     */
    public ResultadoRegion procesarRegiones(Usuario usuario, PlaceInfo placeInfo) {
        ResultadoRegion resultado = new ResultadoRegion();

        if (placeInfo == null) {
            return resultado;
        }

        // Procesar país
        Region pais = obtenerOCrearPais(placeInfo);
        if (pais != null && esPrimeraVisitaPais(usuario, placeInfo.country())) {
            int puntos = registrarPrimeraVisitaregion(usuario, pais, true);
            if (puntos > 0) {
                resultado.addPuntos(puntos);
                resultado.addRegionDescubierta("PAIS", placeInfo.country());
            }
        }

        // Procesar ciudad
        Region ciudad = obtenerOCrearCiudad(placeInfo, pais);
        if (ciudad != null && esPrimeraVisitaCiudad(usuario, placeInfo.city())) {
            int puntos = registrarPrimeraVisitaregion(usuario, ciudad, false);
            if (puntos > 0) {
                resultado.addPuntos(puntos);
                resultado.addRegionDescubierta("CIUDAD", placeInfo.city());
            }
        }

        return resultado;
    }

    /**
     * Obtener estadísticas de regiones visitadas por un usuario
     */
    public long contarPaisesVisitados(Usuario usuario) {
        return usuarioRegionRepository.countByUsuarioAndRegionTipo(usuario, "PAIS");
    }

    public long contarCiudadesVisitadas(Usuario usuario) {
        return usuarioRegionRepository.countByUsuarioAndRegionTipoAndRegionPaisIsNotNull(usuario, "CIUDAD");
    }
}
