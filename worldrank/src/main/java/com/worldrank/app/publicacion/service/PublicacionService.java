package com.worldrank.app.publicacion.service;

import com.worldrank.app.lugar.domain.Lugar;
import com.worldrank.app.lugar.service.LugarService;
import com.worldrank.app.lugar.service.StorageService;
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
    private GeometryFactory geometryFactory;

    public PublicacionService(
            PublicacionRepository publicacionRepository,
            LugarService lugarService,
            VisitaService visitaService,
            StorageService storageService) {

        this.publicacionRepository = publicacionRepository;
        this.lugarService = lugarService;
        this.visitaService = visitaService;
        this.storageService = storageService;
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

            // 2️⃣ Subir imagen
            String urlImagen = storageService.subirImagen(imagenBytes);
            System.out.println("Imagen subida: " + urlImagen);

            // 2️⃣ Obtener lugar
            Lugar lugar = lugarService.obtenerPorId(request.getIdLugar());
            System.out.println("Lugar obtenido: " + lugar.getId());

            // 3️⃣ Crear punto GPS (lng, lat)
            Point gps = geometryFactory.createPoint(
                    new Coordinate(request.getLongitud(), request.getLatitud())
            );
            gps.setSRID(4326);
            System.out.println("GPS creado: " + gps);

            // 4️⃣ Crear publicación
            Publicacion publicacion = new Publicacion();
            publicacion.setId(UUID.randomUUID());
            publicacion.setIdUsuario(idUsuario);
            publicacion.setLugar(lugar);
            publicacion.setDescripcion(request.getDescripcion());
            publicacion.setUrlImagen(urlImagen);
            publicacion.setGps(gps);

            publicacionRepository.save(publicacion);
            System.out.println("Publicacion guardada: " + publicacion.getId());

            // 5️⃣ Puntaje
            VisitaResultado resultado =
                    visitaService.registrarVisita(idUsuario, lugar);
            System.out.println("Visita registrada, puntaje: " + resultado.getPuntaje());

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
