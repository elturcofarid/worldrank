package com.worldrank.app.visita.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.worldrank.app.lugar.domain.Lugar;
import com.worldrank.app.puntaje.service.PuntajeService;
import com.worldrank.app.user.domain.Usuario;
import com.worldrank.app.user.repository.UsuarioRepository;
import com.worldrank.app.visita.domain.Visita;
import com.worldrank.app.visita.repository.VisitaRepository;

@Service
public class VisitaService {

    private VisitaRepository visitaRepository;
    private PuntajeService puntajeService;
    private UsuarioRepository userRepository;

    public VisitaService(VisitaRepository visitaRepository, PuntajeService puntajeService, UsuarioRepository userRepository) {
        this.visitaRepository = visitaRepository;
        this.puntajeService = puntajeService;
        this.userRepository = userRepository;
    }

    //public VisitaResultado registrarVisita(UUID idUsuario, Lugar lugar) {
public int registrarVisita(Usuario usuario, Lugar lugar) {
try{
     // 3️⃣ Validar visita
        boolean yaVisitoReciente =
            visitaRepository.existsByUsuarioAndLugarAndFechaVisitaAfter(
                usuario,
                lugar,
                LocalDateTime.now().minusHours(24)
            );

        if (yaVisitoReciente) {
            return 0; // no suma puntos
        }

        boolean esPrimeraVisita =
            visitaRepository
                .findByUsuarioAndLugar(usuario, lugar)
                .isEmpty();

        // 4️⃣ Calcular puntos
        int puntos = puntajeService.calcularPuntos(
            esPrimeraVisita,
            lugar.getCantidadVisitas(),
            !esPrimeraVisita
        );

        
            
        // 5️⃣ Registrar visita
        Visita visita = Visita.builder()
            .usuario(usuario)
            .lugar(lugar)
            .fechaVisita(LocalDateTime.now())
            .puntosObtenidos(puntos)
            .build();

        visitaRepository.save(visita);
return puntos;

        } catch (Exception e) {
            // Si hay error (ej. trigger de BD), retornar sin puntaje
            System.out.println("Error registrando visita: " + e.getMessage());
            return 0    ;
        }
    }
}

