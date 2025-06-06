/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author eloy.castro
 */
@Embeddable
public class FavoritosPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_anime_FK")
    private int idanimeFK;
    @Basic(optional = false)
    @Column(name = "id_usuario_FK")
    private int idusuarioFK;

    public FavoritosPK() {
    }

    public FavoritosPK(int idanimeFK, int idusuarioFK) {
        this.idanimeFK = idanimeFK;
        this.idusuarioFK = idusuarioFK;
    }

    public int getIdanimeFK() {
        return idanimeFK;
    }

    public void setIdanimeFK(int idanimeFK) {
        this.idanimeFK = idanimeFK;
    }

    public int getIdusuarioFK() {
        return idusuarioFK;
    }

    public void setIdusuarioFK(int idusuarioFK) {
        this.idusuarioFK = idusuarioFK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idanimeFK;
        hash += (int) idusuarioFK;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FavoritosPK)) {
            return false;
        }
        FavoritosPK other = (FavoritosPK) object;
        if (this.idanimeFK != other.idanimeFK) {
            return false;
        }
        if (this.idusuarioFK != other.idusuarioFK) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.vo.FavoritosPK[ idanimeFK=" + idanimeFK + ", idusuarioFK=" + idusuarioFK + " ]";
    }
    
}
