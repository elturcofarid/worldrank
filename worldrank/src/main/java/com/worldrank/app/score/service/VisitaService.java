package com.worldrank.app.score.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.worldrank.app.lugar.domain.Lugar;
import com.worldrank.app.score.domain.Visita;
import com.worldrank.app.score.domain.VisitaResultado;
import com.worldrank.app.score.repository.VisitaRepository;

@Service
public class VisitaService {

    private VisitaRepository visitaRepository;

    public VisitaService(VisitaRepository visitaRepository) {
        this.visitaRepository = visitaRepository;
    }

    public VisitaResultado registrarVisita(UUID idUsuario, Lugar lugar) {

        List<Visita> existente =
                visitaRepository.findByIdUsuarioAndLugar_Id(idUsuario, lugar.getId());

        if (!existente.isEmpty()) {
            return new VisitaResultado(0, false);
        }

        try {
            Visita visita = new Visita();
            visita.setId(UUID.randomUUID());
            visita.setIdUsuario(idUsuario);
            visita.setLugar(lugar);
            visita.setPuntajeOtorgado(lugar.getPuntajeBase());

            visitaRepository.save(visita);

            return new VisitaResultado(lugar.getPuntajeBase(), true);
        } catch (Exception e) {
            // Si hay error (ej. trigger de BD), retornar sin puntaje
            System.out.println("Error registrando visita: " + e.getMessage());
            return new VisitaResultado(0, false);
        }
    }
}

