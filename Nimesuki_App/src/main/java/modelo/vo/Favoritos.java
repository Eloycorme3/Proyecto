/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Formacion3
 */
@Entity
@Table(name = "favoritos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Favoritos.findAll", query = "SELECT f FROM Favoritos f"),
    @NamedQuery(name = "Favoritos.findByIdAnimeFK", query = "SELECT f FROM Favoritos f WHERE f.favoritosPK.idanimeFK = :idanimeFK"),
    @NamedQuery(name = "Favoritos.findByIdUsuarioFK", query = "SELECT f FROM Favoritos f WHERE f.favoritosPK.idusuarioFK = :idusuarioFK"),
    @NamedQuery(name = "Favoritos.findByValoracion", query = "SELECT f FROM Favoritos f WHERE f.valoracion = :valoracion"),
    @NamedQuery(name = "Favoritos.findByCapActual", query = "SELECT f FROM Favoritos f WHERE f.capActual = :capActual")})
public class Favoritos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FavoritosPK favoritosPK;
    @Column(name = "valoracion")
    private Integer valoracion;
    @Column(name = "cap_actual")
    private Integer capActual;
    @JoinColumn(name = "id_anime_FK", referencedColumnName = "id_anime", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Anime anime;
    @JoinColumn(name = "id_usuario_FK", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Favoritos() {
    }

    public Favoritos(FavoritosPK favoritosPK) {
        this.favoritosPK = favoritosPK;
    }

    public Favoritos(int idanimeFK, int idusuarioFK) {
        this.favoritosPK = new FavoritosPK(idanimeFK, idusuarioFK);
    }

    public FavoritosPK getFavoritosPK() {
        return favoritosPK;
    }

    public void setFavoritosPK(FavoritosPK favoritosPK) {
        this.favoritosPK = favoritosPK;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (favoritosPK != null ? favoritosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Favoritos)) {
            return false;
        }
        Favoritos other = (Favoritos) object;
        if ((this.favoritosPK == null && other.favoritosPK != null) || (this.favoritosPK != null && !this.favoritosPK.equals(other.favoritosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return anime.toString();
    }

}
