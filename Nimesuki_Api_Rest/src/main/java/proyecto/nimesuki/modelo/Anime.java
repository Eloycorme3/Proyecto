/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author eloy.castro
 */
@Entity
@Table(name = "anime")
public class Anime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anime")
    private Integer idAnime;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "categorias", length = 150)
    private String categorias;

    @Column(name = "anho_salida")
    private Integer anhoSalida;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "imagen", length = 30)
    private String imagen;

    @Column(name = "cap_totales", nullable = false)
    private Integer capTotales;

    public Anime(Integer idAnime, String nombre, String categorias, Integer anhoSalida, String descripcion, String imagen, Integer capTotales) {
        this.idAnime = idAnime;
        this.nombre = nombre;
        this.categorias = categorias;
        this.anhoSalida = anhoSalida;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.capTotales = capTotales;
    }
    
    public Anime(String nombre) {
        this.nombre = nombre;
    }
    
    public Anime() {
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

    public Integer getCapTotales() {
        return capTotales;
    }

    public void setCapTotales(Integer capTotales) {
        this.capTotales = capTotales;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
