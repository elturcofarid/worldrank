package com.worldrank.app.geocoding;

public record PlaceInfo(
    String name,
    String address,
    double latitude,
    double longitude,
    String category
) {
}