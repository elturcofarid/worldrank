package com.worldrank.app.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worldrank.app.score.repository.VisitaRepository;
import com.worldrank.app.user.domain.Profile;
import com.worldrank.app.user.dto.UserProfileResponse;
import com.worldrank.app.user.dto.VisitedPlaceDto;
import com.worldrank.app.user.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final VisitaRepository visitRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(UUID userId) {

        Profile profile = profileRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        List<VisitedPlaceDto> visitedPlaces =
            visitRepository.findByIdUsuario(userId)
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
