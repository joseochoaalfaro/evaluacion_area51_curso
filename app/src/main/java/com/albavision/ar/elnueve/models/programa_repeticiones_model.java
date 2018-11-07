package com.albavision.ar.elnueve.models;

/**
 * Created by yoshi on 06/09/17.
 */

public class programa_repeticiones_model {
    Integer idprog;
    String fecha, titulo,imgPrograma,vod_yt,vod_mp4,nombre_programa;

    public programa_repeticiones_model(Integer idprog, String fecha, String titulo, String imgPrograma, String vod_yt, String vod_mp4, String nombre_programa) {
        this.idprog = idprog;
        this.fecha = fecha;
        this.titulo = titulo;
        this.imgPrograma = imgPrograma;
        this.vod_yt = vod_yt;
        this.vod_mp4 = vod_mp4;
        this.nombre_programa = nombre_programa;
    }

    public Integer getIdprog() {
        return idprog;
    }

    public void setIdprog(Integer idprog) {
        this.idprog = idprog;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImgPrograma() {
        return imgPrograma;
    }

    public void setImgPrograma(String imgPrograma) {
        this.imgPrograma = imgPrograma;
    }

    public String getVod_yt() {
        return vod_yt;
    }

    public void setVod_yt(String vod_yt) {
        this.vod_yt = vod_yt;
    }

    public String getVod_mp4() {
        return vod_mp4;
    }

    public void setVod_mp4(String vod_mp4) {
        this.vod_mp4 = vod_mp4;
    }

    public String getNombre_programa() {
        return nombre_programa;
    }

    public void setNombre_programa(String nombre_programa) {
        this.nombre_programa = nombre_programa;
    }
}
