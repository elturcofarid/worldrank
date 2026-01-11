package com.worldrank.app.user.dto;

import java.util.UUID;

@lombok.Data
public class VisitedPlaceDto{

     private UUID placeId;

    public VisitedPlaceDto(UUID id) {
        this.placeId = id;
    }

}

   