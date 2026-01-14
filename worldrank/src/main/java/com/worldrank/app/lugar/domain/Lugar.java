package com.worldrank.app.lugar.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lugar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lugar {

    @Id
    @GeneratedValue
    @Column(name = "id_lugar")
    private UUID id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipo_lugar")
    private String tipoLugar;

    @Column(name = "puntaje_base")
    private int puntajeBase;

    @Column(columnDefinition = "geography(Point,4326)")
    private Point geom;

        /**
     * Radio del cluster en metros
     */
    @Column(name = "radio_metros", nullable = false)
    private Integer radioMetros;


      /**
     * Cantidad total de visitas al lugar
     */
    @Column(name = "cantidad_visitas", nullable = false)
    private Integer cantidadVisitas;

       /**
     * Indica si el lugar fue generado automáticamente
     */
    @Column(name = "auto_generado", nullable = false)
    private Boolean autoGenerado;



    /**
     * Fecha de creación del lugar
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Última vez que se visitó el lugar
     */
    @Column(name = "ultima_visita")
    private LocalDateTime ultimaVisita;

    /**
     * Nivel de confianza del lugar (0–100)
     */
    @Column(name = "confianza")
    private Integer confianza;



    // ========================
    // getters y setters
    // ========================
/*

    public UUID getId() {
        return id;
    }

    public int getPuntajeBase() {
        return puntajeBase;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipoLugar(String tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public void setPuntajeBase(int puntajeBase) {
        this.puntajeBase = puntajeBase;
    }

    public void setGeom(Point geom) {
        this.geom = geom;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    */

   
}
