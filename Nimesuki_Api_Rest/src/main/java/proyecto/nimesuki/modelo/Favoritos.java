/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author eloy.castro
 */
@Entity
@Table(name = "favoritos")
public class Favoritos implements Serializable {

    @EmbeddedId
    private FavoritosId id;

    @ManyToOne
    @JoinColumn(name = "id_anime_FK", insertable = false, updatable = false)
    private Anime anime;

    @ManyToOne
    @JoinColumn(name = "id_usuario_FK", insertable = false, updatable = false)
    private Usuario usuario;

    @Column
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
