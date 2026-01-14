package com.worldrank.app.user.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.worldrank.app.user.domain.Profile;
import com.worldrank.app.user.domain.Usuario;
import com.worldrank.app.user.dto.UserProfileResponse;
import com.worldrank.app.user.dto.VisitedPlaceDto;
import com.worldrank.app.user.repository.ProfileRepository;
import com.worldrank.app.user.repository.UsuarioRepository;
import com.worldrank.app.visita.repository.VisitaRepository;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;
    private VisitaRepository visitRepository;
    private UsuarioRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, VisitaRepository visitRepository, UsuarioRepository userRepository) {
        this.profileRepository = profileRepository;
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;   
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(UUID userId) {

        Profile profile = profileRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

                    Usuario usuario = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); 

        List<VisitedPlaceDto> visitedPlaces =
            visitRepository.findByUsuario(usuario)
                .stream()
                .map(v -> new VisitedPlaceDto(v.getLugar().getId()))
                .toList();

        return new UserProfileResponse(
            userId,
            profile.getScore(),
            profile.getRango().getNombre(),
            profile.getBiografia(),
            profile.getFotoUrl(),       
            visitedPlaces
        );
    }
}
