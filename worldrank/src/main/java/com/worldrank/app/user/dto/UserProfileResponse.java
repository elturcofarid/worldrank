package com.worldrank.app.user.dto;

import java.util.List;
import java.util.UUID;

public record UserProfileResponse(
    UUID userId,
    Integer puntaje,
    String rango,
    String biografia,
    String fotoUrl,
    List<VisitedPlaceDto> lugaresVisitados
) {}