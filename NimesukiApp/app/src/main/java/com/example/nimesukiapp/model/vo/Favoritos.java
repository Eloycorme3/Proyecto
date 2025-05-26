package com.example.nimesukiapp.model.vo;

import java.io.Serializable;

public class Favoritos implements Serializable {

    private FavoritosId id;

    private Anime anime;

    private Usuario usuario;

    private Integer valoracion;

    private Integer capActual;

    public Favoritos(FavoritosId id, Anime anime, Usuario usuario, Integer valoracion, Integer capActual) {
        this.id = id;
        this.anime = anime;
        this.usuario = usuario;
        this.valoracion = valoracion;
        this.capActual = capActual;
    }

    public Favoritos(Anime anime) {
        this.anime = anime;
    }

    public Favoritos() {
    }

    public FavoritosId getId() {
        return id;
    }

    public void setId(FavoritosId id) {
        this.id = id;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }

    public Integer getCapActual() {
        return capActual;
    }

    public void setCapActual(Integer capActual) {
        this.capActual = capActual;
    }

    @Override
    public String toString() {
        return anime.getNombre();
    }

}
