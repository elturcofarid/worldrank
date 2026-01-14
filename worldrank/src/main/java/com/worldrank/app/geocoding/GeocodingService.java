package com.worldrank.app.geocoding;

import java.util.List;

public interface GeocodingService {
    List<PlaceInfo> searchPlaces(String query);
    PlaceInfo reverseGeocode(double latitude, double longitude);
}