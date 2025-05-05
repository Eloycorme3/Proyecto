package com.example.nimesukiapp.models.vo;

import java.io.Serializable;

public class Anime implements Serializable {

    private int idAnime;
    private String nombre;
    private String categorias;
    private int anhoSalida;
    private String descripcion;
    private String imagen;
    private int capTotales;

    // Constructor
    public Anime(int idAnime, String nombre, String categorias, int anhoSalida, String descripcion, String imagen, int capTotales) {
        this.idAnime = idAnime;
        this.nombre = nombre;
        this.categorias = categorias;
        this.anhoSalida = anhoSalida;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.capTotales = capTotales;
    }

    public int getIdAnime() {
        return idAnime;
    }

    public void setIdAnime(int idAnime) {
        this.idAnime = idAnime;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    public int getAnhoSalida() {
        return anhoSalida;
    }

    public void setAnhoSalida(int anhoSalida) {
        this.anhoSalida = anhoSalida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCapTotales() {
        return capTotales;
    }

    public void setCapTotales(int capTotales) {
        this.capTotales = capTotales;
    }
}