package com.worldrank.app.geocoding;

public record PlaceInfo(
    String name,
    String address,
    double latitude,
    double longitude,
    String category,
    String country,
    String city,
    String countryCode
) {
    /**
     * Verifica si el lugar tiene información válida conocida
     */
    public boolean esLugarConocido() {
        return name != null && !name.isEmpty() && 
               !name.equalsIgnoreCase("Unknown Place") &&
               !name.equalsIgnoreCase("Unnamed");
    }
    
    /**
     * Obtiene el nombre del país o el código ISO si el nombre no está disponible
     */
    public String getPaisDisplay() {
        return country != null && !country.isEmpty() ? country : 
               countryCode != null ? countryCode.toUpperCase() : "Desconocido";
    }
    
    /**
     * Obtiene el nombre de la ciudad o un valor por defecto
     */
    public String getCiudadDisplay() {
        return city != null && !city.isEmpty() ? city : "Desconocida";
    }
}