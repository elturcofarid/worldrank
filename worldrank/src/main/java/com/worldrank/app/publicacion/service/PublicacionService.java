package com.worldrank.app.publicacion.service;

import com.worldrank.app.lugar.domain.Lugar;
import com.worldrank.app.lugar.service.LugarService;
import com.worldrank.app.lugar.service.StorageService;
import com.worldrank.app.geocoding.GeocodingService;
import com.worldrank.app.publicacion.controller.CrearPublicacionRequest;
import com.worldrank.app.publicacion.controller.PublicacionResponse;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import com.worldrank.app.publicacion.domain.Publicacion;
import com.worldrank.app.publicacion.repository.PublicacionRepository;
import com.worldrank.app.score.domain.VisitaResultado;
import com.worldrank.app.score.service.VisitaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
    private GeometryFactory geometryFactory;

    public PublicacionService(
            PublicacionRepository publicacionRepository,
            LugarService lugarService,
            VisitaService visitaService,
            StorageService storageService,
            ImageMetadataService imageMetadataService,
            GeocodingService geocodingService) {

        this.publicacionRepository = publicacionRepository;
        this.lugarService = lugarService;
        this.visitaService = visitaService;
        this.storageService = storageService;
        this.imageMetadataService = imageMetadataService;
        this.geocodingService = geocodingService;
        this.geometryFactory = new GeometryFactory();
    }

    public PublicacionResponse crearPublicacion(
            UUID idUsuario,
            CrearPublicacionRequest request) {
        try {
            System.out.println("Creando publicacion para usuario: " + idUsuario);

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
            String urlImagen = storageService.subirImagen(imagenBytes);
            System.out.println("Imagen subida: " + urlImagen);

            // 4️⃣ Crear lugar usando geocoding
            String nombreLugar = "Ubicación desconocida";
            try {
                var place = geocodingService.reverseGeocode(latitud, longitud);
                if (place != null) {
                    nombreLugar = place.name();
                }
            } catch (Exception e) {
                System.out.println("Error en geocoding: " + e.getMessage());
            }
            Lugar lugar = lugarService.crearLugar(nombreLugar, "Fotografía", 10, longitud, latitud);
            System.out.println("Lugar creado: " + lugar.getId());

            // 5️⃣ Crear punto GPS (lng, lat)
            Point gps = geometryFactory.createPoint(
                    new Coordinate(longitud.doubleValue(), latitud.doubleValue())
            );
            gps.setSRID(4326);
            System.out.println("GPS creado: " + gps);

            // 6️⃣ Puntaje
            VisitaResultado resultado =
                    visitaService.registrarVisita(idUsuario, lugar);
            System.out.println("Visita registrada, puntaje: " + resultado.getPuntaje());

            // 7️⃣ Crear publicación
            Publicacion publicacion = new Publicacion();
            publicacion.setId(UUID.randomUUID());
            publicacion.setIdUsuario(idUsuario);
            publicacion.setLugar(lugar);
            publicacion.setDescripcion(request.getDescripcion());
            publicacion.setUrlImagen(urlImagen);
            publicacion.setGps(gps);

            publicacionRepository.save(publicacion);
            System.out.println("Publicacion guardada: " + publicacion.getId());

            return new PublicacionResponse(
                    publicacion.getId(),
                    idUsuario,
                    lugar.getId(),
                    request.getDescripcion(),
                    urlImagen,
                    publicacion.getFechaPublicacion(),
                    resultado.getPuntaje(),
                    resultado.isLugarNuevo()
            );
        } catch (Exception e) {
            System.err.println("Error creando publicacion: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
