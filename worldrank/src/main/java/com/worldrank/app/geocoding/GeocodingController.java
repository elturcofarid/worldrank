package com.worldrank.app.geocoding;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geocoding")
public class GeocodingController {

    private final GeocodingService geocodingService;

    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlaceInfo>> searchPlaces(@RequestParam String q) {
        List<PlaceInfo> places = geocodingService.searchPlaces(q);
        return ResponseEntity.ok(places);
    }

    @GetMapping("/reverse")
    public ResponseEntity<PlaceInfo> reverseGeocode(@RequestParam double lat, @RequestParam double lon) {
        PlaceInfo place = geocodingService.reverseGeocode(lat, lon);
        return place != null ? ResponseEntity.ok(place) : ResponseEntity.notFound().build();
    }
}