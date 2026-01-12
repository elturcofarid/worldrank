package com.worldrank.app.geocoding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapboxGeocodingService implements GeocodingService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String accessToken;

    public MapboxGeocodingService(@Value("${mapbox.access.token}") String accessToken) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.accessToken = accessToken;
    }

    @Override
    public List<PlaceInfo> searchPlaces(String query) {
        String url = UriComponentsBuilder.fromUriString("https://api.mapbox.com/geocoding/v5/mapbox.places/{query}.json")
                .queryParam("access_token", accessToken)
                .buildAndExpand(query)
                .toUriString();
        return parseResponse(url);
    }

    @Override
    public PlaceInfo reverseGeocode(double latitude, double longitude) {
        String url = UriComponentsBuilder.fromUriString("https://api.mapbox.com/geocoding/v5/mapbox.places/{lon},{lat}.json")
                .queryParam("access_token", accessToken)
                .buildAndExpand(longitude, latitude)
                .toUriString();
        List<PlaceInfo> places = parseResponse(url);
        return places.isEmpty() ? null : places.get(0);
    }

    private List<PlaceInfo> parseResponse(String url) {
        List<PlaceInfo> places = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode features = root.path("features");
            for (JsonNode feature : features) {
                String name = feature.path("text").asText();
                String address = feature.path("place_name").asText();
                JsonNode center = feature.path("center");
                double lon = center.get(0).asDouble();
                double lat = center.get(1).asDouble();
                String category = feature.path("properties").path("category").asText();
                places.add(new PlaceInfo(name, address, lat, lon, category));
            }
        } catch (Exception e) {
            // Log error
            e.printStackTrace();
        }
        return places;
    }
}