package com.worldrank.app.score.domain;

public class VisitaResultado {
    public VisitaResultado(int puntaje, boolean lugarNuevo) {
        this.puntaje = puntaje;
        this.lugarNuevo = lugarNuevo;
    }
    private int puntaje;
    private boolean lugarNuevo;

    public int getPuntaje() {
        return puntaje;
    }

    public boolean isLugarNuevo() {
        return lugarNuevo;
    }
}
