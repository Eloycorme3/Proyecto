/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import modelo.vo.Anime;
import modelo.vo.Favoritos;
import modelo.vo.Usuario;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author eloy.castro
 */
public class FavoritosDAO {

    public void cargarComboPorUsuario(Session session, DefaultComboBoxModel modeloComboAnimesFavoritos, Usuario u) {
        modeloComboAnimesFavoritos.removeAllElements();
        Favoritos f;

        String namedQuery = "Favoritos.findByIdUsuarioFK";
        Query q = session.createNamedQuery(namedQuery);
        q.setParameter("idusuarioFK", u.getIdUsuario());

        Iterator it = q.list().iterator();
        while (it.hasNext()) {
            f = (Favoritos) it.next();
            modeloComboAnimesFavoritos.addElement(f);
        }
    }
    
    public void borrarFavorito(Session session, Favoritos f) {
        session.delete(f);
    }
    
    public List<Favoritos> obtenerFavoritosPorUsuario(Session session, Usuario u) {
        String namedQuery = "Favoritos.findByIdUsuarioFK";
        Query q = session.createNamedQuery(namedQuery);
        q.setParameter("idusuarioFK", u.getIdUsuario());

        List<Favoritos> favoritos = (List<Favoritos>) q.list();
        
        return favoritos;
    }

    public void darAltaFavorito(Session session, Favoritos f) {
        session.save(f);
    }

    public Favoritos buscarFavorito(Session session, Anime a, Usuario u) {
        Favoritos f = null;
        String query = "Favoritos.findByIdUsuarioFK&IdAnimeFK";
        Query q = session.createNamedQuery(query);
        
        q.setParameter("idusuarioFK", u.getIdUsuario());
        q.setParameter("idanimeFK", a.getIdAnime());
        
        Iterator it = q.list().iterator();
        if (it.hasNext()) {
            f = (Favoritos) it.next();
        }
        return f;
    }

    public void modificarFavorito(Session session, Favoritos f, float nuevaValoracion, int nuevoCapActual) {
        f.setValoracion(nuevaValoracion);
        f.setCapActual(nuevoCapActual);
        session.evict(f);
        session.update(f);
    }

    public void darBajaFavorito(Session session, Favoritos f) {
        session.delete(f);
    }

    public void modificarFavoritosCambioEpisodios(Session session, Anime a) {
        Favoritos f = null;
        String query = "Favoritos.findByIdAnimeFK";
        Query q = session.createNamedQuery(query);
        
        q.setParameter("idanimeFK", a.getIdAnime());
        
        Iterator it = q.list().iterator();
        while (it.hasNext()) {
            f = (Favoritos) it.next();
            if (f.getCapActual() > a.getCapTotales()) {
                f.setCapActual(a.getCapTotales());
                session.evict(f);
                session.update(f);
            }
        }
    }

    public List<Favoritos> buscarFavoritosPorAnime(Session session, Anime a) {
        String query = "Favoritos.findByIdAnimeFK";
        Query q = session.createNamedQuery(query);
        q.setParameter("idanimeFK", a.getIdAnime());
        
        List<Favoritos> favoritos = (List<Favoritos>) q.list();
        return favoritos;
    }
    

    
}
