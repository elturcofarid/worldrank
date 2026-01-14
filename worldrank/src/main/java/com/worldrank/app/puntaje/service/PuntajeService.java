package com.worldrank.app.puntaje.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuntajeService {

    private static final int BASE_POINTS = 100;

    public int calcularPuntos(
        boolean esPrimeraVisita,
        int visitasLugar,
        boolean revisitaDespuesDe24h
    ) {

        int puntos = 0;

        if (esPrimeraVisita) {
            puntos += BASE_POINTS;
        }

        if (visitasLugar < 10) {
            puntos += 20; // lugar poco explorado
        } else if (visitasLugar > 100) {
            puntos += 5; // lugar popular
        }

        if (revisitaDespuesDe24h) {
            puntos += 10;
        }

        return puntos;
    }
}