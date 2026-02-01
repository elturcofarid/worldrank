package com.worldrank.app.publicacion.service;

import com.worldrank.app.lugar.domain.Lugar;
import com.worldrank.app.lugar.service.LugarService;
import com.worldrank.app.lugar.service.StorageService;
import com.worldrank.app.geocoding.GeocodingService;
import com.worldrank.app.geocoding.PlaceInfo;
import com.worldrank.app.publicacion.controller.CrearPublicacionRequest;
import com.worldrank.app.publicacion.controller.PublicacionResponse;
import com.worldrank.app.publicacion.dto.PublicacionListResponse;
import com.worldrank.app.publicacion.dto.UsuarioSummary;
import com.worldrank.app.region.service.RegionService;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import com.worldrank.app.publicacion.domain.Publicacion;
import com.worldrank.app.publicacion.repository.PublicacionRepository;
import com.worldrank.app.user.domain.Profile;
import com.worldrank.app.user.domain.Usuario;
import com.worldrank.app.user.repository.ProfileRepository;
import com.worldrank.app.user.repository.UsuarioRepository;
import com.worldrank.app.visita.service.VisitaService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Base64;
import java.util.UUID;

@Service
@Transactional
public class PublicacionService {

    private PublicacionRepository publicacionRepository;
    private LugarService lugarService;
    private VisitaService visitaService;
    private StorageService storageService;
    private ImageMetadataService imageMetadataService;
    private GeocodingService geocodingService;
    private RegionService regionService;
    private ProfileRepository profileRepository;
    private UsuarioRepository usuarioRepository;
    private GeometryFactory geometryFactory;

    public PublicacionService(
            PublicacionRepository publicacionRepository,
            LugarService lugarService,
            VisitaService visitaService,
            StorageService storageService,
            ImageMetadataService imageMetadataService,
            GeocodingService geocodingService,
            RegionService regionService,
            ProfileRepository profileRepository,
            UsuarioRepository usuarioRepository) {

        this.publicacionRepository = publicacionRepository;
        this.lugarService = lugarService;
        this.visitaService = visitaService;
        this.storageService = storageService;
        this.imageMetadataService = imageMetadataService;
        this.geocodingService = geocodingService;
        this.regionService = regionService;
        this.profileRepository = profileRepository;
        this.usuarioRepository = usuarioRepository;
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    public PublicacionResponse crearPublicacion(
            Usuario usuario,
            CrearPublicacionRequest request) {
        try {
            System.out.println("Creando publicacion para usuario: " + usuario);

            // 1️⃣ Decodificar imagen base64
            String base64Data = request.imagenBase64();
            if (base64Data.startsWith("data:")) {
                int commaIndex = base64Data.indexOf(',');
                if (commaIndex != -1) {
                    base64Data = base64Data.substring(commaIndex + 1);
                }
            }
            byte[] imagenBytes = Base64.getDecoder().decode(base64Data);

            // 2️⃣ Extraer coordenadas GPS de la imagen si no están proporcionadas
            Double longitud = request.getLongitud();
            Double latitud = request.getLatitud();
            if (longitud == null || latitud == null) {
                try {
                    double[] gpsCoords = imageMetadataService.extractGpsCoordinates(imagenBytes);
                    if (gpsCoords != null) {
                        longitud = gpsCoords[0];
                        latitud = gpsCoords[1];
                        System.out.println("Coordenadas GPS extraídas de la imagen: " + longitud + ", " + latitud);
                    } else {
                        System.out.println("No se encontraron coordenadas GPS en la imagen");
                    }
                } catch (Exception e) {
                    System.out.println("Error extrayendo GPS de la imagen: " + e.getMessage());
                }
            }

            // 3️⃣ Subir imagen
            String urlImagen = storageService.subirImagen(imagenBytes, "publicaciones", usuario.getId());
            System.out.println("Imagen subida: " + urlImagen);


            // 4️⃣ Obtener información del lugar mediante geocoding
            PlaceInfo placeInfo = null;
            if (latitud != null && longitud != null) {
                try {
                    placeInfo = geocodingService.reverseGeocode(latitud, longitud);
                    if (placeInfo != null) {
                        System.out.println("Geocoding: " + placeInfo.name() + " (" + placeInfo.category() + ")");
                        if (placeInfo.country() != null) {
                            System.out.println("País: " + placeInfo.country() + " (" + placeInfo.countryCode() + ")");
                        }
                        if (placeInfo.city() != null) {
                            System.out.println("Ciudad: " + placeInfo.city());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error en geocoding: " + e.getMessage());
                }
            }

            // 5️⃣ Crear punto GPS (lng, lat)
            Point gps = geometryFactory.createPoint(
                    new Coordinate(longitud != null ? longitud.doubleValue() : 0, 
                                   latitud != null ? latitud.doubleValue() : 0)
            );
            gps.setSRID(4326);

            // 6️⃣ Obtener o crear lugar con información del geocoding
            Lugar lugar = lugarService.obtenerLugarCercano(gps, placeInfo);
            System.out.println("Lugar: " + lugar.getId() + " - " + lugar.getNombre());

            // 7️⃣ Crear publicación
            Publicacion publicacion = new Publicacion();
            publicacion.setId(UUID.randomUUID());
            publicacion.setIdUsuario(usuario.getId());
            publicacion.setLugar(lugar);
            publicacion.setDescripcion(request.getDescripcion());
            publicacion.setUrlImagen(obtenerBucketYPathDeUrl(urlImagen));
            publicacion.setGps(gps);

            publicacionRepository.save(publicacion);
            System.out.println("Publicacion guardada: " + publicacion.getId());


            // 8️⃣ Registra visita y obtiene Puntaje
            int resultado = visitaService.registrarVisita(usuario, lugar);
            System.out.println("Visita registrada, puntaje: " + resultado);

            // 9️⃣ Procesar regiones (país/ciudad) y sumar puntos adicionales
            if (placeInfo != null) {
                var resultadoRegion = regionService.procesarRegiones(usuario, placeInfo);
                if (resultadoRegion.tieneRegionesNuevas()) {
                    resultado += resultadoRegion.getTotalPuntos();
                    System.out.println("Regiones descubiertas: " + resultadoRegion.getRegionesDescubiertas() + ", puntos adicionales: " + resultadoRegion.getTotalPuntos());
                }
            }

            return new PublicacionResponse(
                    publicacion.getId(),
                    usuario.getId(),
                    lugar.getId(),
                    request.getDescripcion(),
                    urlImagen,
                    publicacion.getFechaPublicacion(),
                    resultado,
                    false
            );
        } catch (Exception e) {
            System.err.println("Error creando publicacion: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Page<PublicacionListResponse> obtenerPublicaciones(Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.findAll(pageable);
        return publicaciones.map(publicacion -> {
            Usuario usuario = usuarioRepository.findById(publicacion.getIdUsuario()).orElse(null);
            if (usuario == null) return null;

            Profile profile = profileRepository.findByUserId(usuario.getId()).orElse(null);
            String nombre = profile != null ? usuario.getUsername() : usuario.getEmail();
            String avatarUrl = profile != null ? profile.getFotoUrl() : null;

            UsuarioSummary usuarioSummary = new UsuarioSummary(
                usuario.getId(),
                nombre,
                avatarUrl
            );

            return new PublicacionListResponse(
                publicacion.getId(),
                publicacion.getDescripcion(),
                publicacion.getUrlImagen(),
                publicacion.getGps() != null ? publicacion.getGps().getX() : null,
                publicacion.getGps() != null ? publicacion.getGps().getY() : null,
                publicacion.getFechaPublicacion(),
                usuarioSummary
            );
        });
    }

    //crear un metodo que obtenga de la url de la imagen el bucket y el path de la imagen, https://198.162.1.135/worldrank-publicaciones/usuario-id/imagen.jpg
    // y retornar solo usuario-id/imagen.jpg
    private String obtenerBucketYPathDeUrl(String urlImagen) {
        try {
            java.net.URL url = new java.net.URL(urlImagen);
            String path = url.getPath(); // /worldrank-publicaciones/usuario-id/imagen.jpg
            if (path.startsWith("/")) {
                path = path.substring(1); // worldrank-publicaciones/usuario-id/imagen.jpg
            }
            int firstSlashIndex = path.indexOf('/');
            if (firstSlashIndex != -1) {
                return path.substring(firstSlashIndex + 1); // usuario-id/imagen.jpg
            } else {
                return path; // en caso de que no haya más slashes
            }
        } catch (Exception e) {
            System.err.println("Error al parsear la URL de la imagen: " + e.getMessage());
            return urlImagen; // retornar la URL completa en caso de error
        }
    
    }
}
