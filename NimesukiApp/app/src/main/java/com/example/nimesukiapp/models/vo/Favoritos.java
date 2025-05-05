package com.example.nimesukiapp.models.vo;

import java.io.Serializable;

public class Favoritos implements Serializable {

    private int idAnime;
    private int idUsuario;
    private int valoracion;
    private int capActual;

    // Constructor
    public Favoritos(int idAnime, int idUsuario, int valoracion, int capActual) {
        this.idAnime = idAnime;
        this.idUsuario = idUsuario;
        this.valoracion = valoracion;
        this.capActual = capActual;
    }

    // Getters y Setters
    public int getIdAnime() {
        return idAnime;
    }

    public void setIdAnime(int idAnime) {
        this.idAnime = idAnime;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public int getCapActual() {
        return capActual;
    }

    public void setCapActual(int capActual) {
        this.capActual = capActual;
    }
}

