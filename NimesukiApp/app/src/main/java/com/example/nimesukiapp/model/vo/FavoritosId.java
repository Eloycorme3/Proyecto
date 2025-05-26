package com.example.nimesukiapp.model.vo;

import java.io.Serializable;
import java.util.Objects;

public class FavoritosId implements Serializable {

    private Integer usuario;

    private Integer anime;

    public FavoritosId() {
    }

    public FavoritosId(Integer usuario, Integer anime) {
        this.usuario = usuario;
        this.anime = anime;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public Integer getAnime() {
        return anime;
    }

    public void setAnime(Integer anime) {
        this.anime = anime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoritosId that = (FavoritosId) o;
        return usuario.equals(that.usuario) && anime.equals(that.anime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, anime);
    }
}
