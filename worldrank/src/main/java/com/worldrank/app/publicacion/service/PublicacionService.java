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
import java.util.UUID;

@Service
@Transactional
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final LugarService lugarService;
    private final VisitaService visitaService;
    private final StorageService storageService;
    private final GeometryFactory geometryFactory;

    public PublicacionService(
            PublicacionRepository publicacionRepository,
            LugarService lugarService,
            VisitaService visitaService,
            StorageService storageService,
            GeometryFactory geometryFactory) {

        this.publicacionRepository = publicacionRepository;
        this.lugarService = lugarService;
        this.visitaService = visitaService;
        this.storageService = storageService;
        this.geometryFactory = geometryFactory;
    }

    public PublicacionResponse crearPublicacion(
            UUID idUsuario,
            MultipartFile imagen,
            CrearPublicacionRequest request) {

        // 1️⃣ Subir imagen
        String urlImagen = storageService.subirImagen(imagen);

        // 2️⃣ Obtener lugar
        Lugar lugar = lugarService.obtenerPorId(request.getIdLugar());

        // 3️⃣ Crear punto GPS (lng, lat)
        Point gps = geometryFactory.createPoint(
                new Coordinate(request.getLongitud(), request.getLatitud())
        );

        // 4️⃣ Crear publicación
        Publicacion publicacion = new Publicacion();
        publicacion.setIdUsuario(idUsuario);
        publicacion.setLugar(lugar);
        publicacion.setDescripcion(request.getDescripcion());
        publicacion.setUrlImagen(urlImagen);
        publicacion.setGps(gps);

        publicacionRepository.save(publicacion);

        // 5️⃣ Puntaje
        VisitaResultado resultado =
                visitaService.registrarVisita(idUsuario, lugar);

        return new PublicacionResponse(
                publicacion.getId(),
                publicacion.getFechaPublicacion(),
                resultado.getPuntaje(),
                resultado.isLugarNuevo()
        );
    }
}
