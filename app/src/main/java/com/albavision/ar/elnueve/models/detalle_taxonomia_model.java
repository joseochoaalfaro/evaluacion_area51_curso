package com.albavision.ar.elnueve.models;

/**
 * Created by yoshi on 18/08/17.
 */

public class detalle_taxonomia_model {
    Integer idTaxonomia;
    String nombre, ruta;

    public detalle_taxonomia_model(Integer idTaxonomia, String nombre,String ruta) {
        this.idTaxonomia = idTaxonomia;
        this.nombre = nombre;
        this.ruta = ruta;
    }

    public Integer getIdTaxonomia() {
        return idTaxonomia;
    }

    public void setIdTaxonomia(Integer idTaxonomia) {
        this.idTaxonomia = idTaxonomia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
