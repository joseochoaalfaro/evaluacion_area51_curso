package com.albavision.ar.elnueve.models;

import org.json.JSONArray;

/**
 * Created by yoshi on 10/08/17.
 */

public class main_noticia_model {
    private String categoria,titulo,url_imagen,fecha,url_imagen3,contenido,video,tipo;
    private Integer codigo,codcat;
    private JSONArray taxonomias;

    public main_noticia_model(String categoria, String titulo, String url_imagen, Integer codigo, Integer codcat, String fecha, String url_imagen3, String contenido, JSONArray taxonomias,String video,String tipo) {
        this.categoria = categoria;
        this.titulo = titulo;
        this.url_imagen = url_imagen;
        this.codigo = codigo;
        this.codcat = codcat;
        this.fecha = fecha;
        this.url_imagen3 = url_imagen3;
        this.contenido = contenido;
        this.taxonomias = taxonomias;
        this.video= video;
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() { return titulo; }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodcat() { return codcat; }

    public void setCodcat(Integer codcat) { this.codcat = codcat; }

    public String getFecha() { return fecha; }

    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getUrl_imagen3() {
        return url_imagen3;
    }

    public void setUrl_imagen3(String url_imagen3) {
        this.url_imagen3 = url_imagen3;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public JSONArray getTaxonomias() {
        return taxonomias;
    }

    public void setTaxonomias(JSONArray taxonomias) {
        this.taxonomias = taxonomias;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
