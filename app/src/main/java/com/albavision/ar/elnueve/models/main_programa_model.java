package com.albavision.ar.elnueve.models;

/**
 * Created by yoshi on 14/08/17.
 */

public class main_programa_model {
    Integer idprog;
    String tituloProg,logo, frecuenciaProg,imgPrograma,noticias,clips,repeticiones;

    public main_programa_model(Integer idprog, String tituloProg,String logo, String frecuenciaProg, String imgPrograma, String noticias, String clips, String repeticiones) {
        this.idprog = idprog;
        this.tituloProg = tituloProg;
        this.logo = logo;
        this.frecuenciaProg = frecuenciaProg;
        this.imgPrograma = imgPrograma;
        this.noticias = noticias;
        this.clips = clips;
        this.repeticiones = repeticiones;
    }

    public Integer getIdprog() {
        return idprog;
    }

    public void setIdprog(Integer idprog) {
        this.idprog = idprog;
    }

    public String getTituloProg() {
        return tituloProg;
    }

    public void setTituloProg(String tituloProg) {
        this.tituloProg = tituloProg;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFrecuenciaProg() {
        return frecuenciaProg;
    }

    public void setFrecuenciaProg(String frecuenciaProg) {
        this.frecuenciaProg = frecuenciaProg;
    }

    public String getImgPrograma() {
        return imgPrograma;
    }

    public void setImgPrograma(String imgPrograma) {
        this.imgPrograma = imgPrograma;
    }

    public String getClips() {
        return clips;
    }

    public void setClips(String clips) {
        this.clips = clips;
    }

    public String getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(String repeticiones) {
        this.repeticiones = repeticiones;
    }

    public String getNoticias() {
        return noticias;
    }

    public void setNoticias(String noticias) {
        this.noticias = noticias;
    }
}
