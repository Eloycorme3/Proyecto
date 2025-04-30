/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eloy.castro
 */
@Entity
@Table(name = "anime")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Anime.findAll", query = "SELECT a FROM Anime a"),
    @NamedQuery(name = "Anime.findByIdAnime", query = "SELECT a FROM Anime a WHERE a.idAnime = :idAnime"),
    @NamedQuery(name = "Anime.findByNombre", query = "SELECT a FROM Anime a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Anime.findByCategorias", query = "SELECT a FROM Anime a WHERE a.categorias = :categorias"),
    @NamedQuery(name = "Anime.findByAnhoSalida", query = "SELECT a FROM Anime a WHERE a.anhoSalida = :anhoSalida"),
    @NamedQuery(name = "Anime.findByDescripcion", query = "SELECT a FROM Anime a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "Anime.findByImagen", query = "SELECT a FROM Anime a WHERE a.imagen = :imagen"),
    @NamedQuery(name = "Anime.findByCapTotales", query = "SELECT a FROM Anime a WHERE a.capTotales = :capTotales")})
public class Anime implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_anime")
    private Integer idAnime;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "categorias")
    private String categorias;
    @Column(name = "anho_salida")
    private Integer anhoSalida;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "imagen")
    private String imagen;
    @Basic(optional = false)
    @Column(name = "cap_totales")
    private int capTotales;
    @OneToMany(mappedBy = "anime")
    private List<Favoritos> favoritosList;

    public Anime() {
    }

    public Anime(Integer idAnime) {
        this.idAnime = idAnime;
    }

    public Anime(Integer idAnime, String nombre, String descripcion, int capTotales) {
        this.idAnime = idAnime;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.capTotales = capTotales;
        this.favoritosList = new ArrayList<>();
    }

    public Integer getIdAnime() {
        return idAnime;
    }

    public void setIdAnime(Integer idAnime) {
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

    public Integer getAnhoSalida() {
        return anhoSalida;
    }

    public void setAnhoSalida(Integer anhoSalida) {
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

    @XmlTransient
    public List<Favoritos> getFavoritosList() {
        return favoritosList;
    }

    public void setFavoritosList(List<Favoritos> favoritosList) {
        this.favoritosList = favoritosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnime != null ? idAnime.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Anime)) {
            return false;
        }
        Anime other = (Anime) object;
        if ((this.idAnime == null && other.idAnime != null) || (this.idAnime != null && !this.idAnime.equals(other.idAnime))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
