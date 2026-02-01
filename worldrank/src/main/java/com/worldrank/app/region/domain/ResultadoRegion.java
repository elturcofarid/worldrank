package com.worldrank.app.region.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para retornar el resultado del procesamiento de regiones
 */
public class ResultadoRegion {

    private int totalPuntos;
    private List<String> regionesDescubiertas;

    public ResultadoRegion() {
        this.totalPuntos = 0;
        this.regionesDescubiertas = new ArrayList<>();
    }

    public void addPuntos(int puntos) {
        this.totalPuntos += puntos;
    }

    public void addRegionDescubierta(String tipo, String nombre) {
        this.regionesDescubiertas.add(tipo + ": " + nombre);
    }

    public int getTotalPuntos() {
        return totalPuntos;
    }

    public List<String> getRegionesDescubiertas() {
        return regionesDescubiertas;
    }

    public boolean tieneRegionesNuevas() {
        return !regionesDescubiertas.isEmpty();
    }
}
