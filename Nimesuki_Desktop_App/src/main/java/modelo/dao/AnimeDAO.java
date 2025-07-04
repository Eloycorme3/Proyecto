/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import modelo.vo.Anime;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author eloy.castro
 */
public class AnimeDAO {

    public void cargarCombo(Session session, DefaultComboBoxModel modeloComboAnimesFavoritos) {
        modeloComboAnimesFavoritos.removeAllElements();
        Anime a;

        String namedQuery = "Anime.findAll";
        Query q = session.createNamedQuery(namedQuery);

        Iterator it = q.list().iterator();
        while (it.hasNext()) {
            a = (Anime) it.next();
            modeloComboAnimesFavoritos.addElement(a);
        }
    }

    public Anime buscarAnimePorNombre(Session session, String nombre) {
        Anime a = null;

        String namedQuery = "Anime.findByNombre";
        Query q = session.createNamedQuery(namedQuery);
        q.setParameter("nombre", nombre);

        Iterator it = q.list().iterator();
        while (it.hasNext()) {
            a = (Anime) it.next();
        }
        return a;
    }

    public void cargarAreaAnimes(Session session, JTextArea taPrimerListadoCompleto) {
        taPrimerListadoCompleto.setText("");
        String query = "Anime.findAll";
        Query q = session.createNamedQuery(query);

        if (q.list().isEmpty()) {
            taPrimerListadoCompleto.setText("No se encontraron coincidencias.");
            return;
        }

        Iterator it = q.list().iterator();
        while (it.hasNext()) {
            Anime a = (Anime) it.next();
            taPrimerListadoCompleto.append(a.toString());
            if (it.hasNext()) {
                taPrimerListadoCompleto.append("\n");
            }
        }
    }

    public void cargarAreaAnimesPorNombreYCapitulosMaximos(Session session, JTextArea taSegundoListado, String nombre, int capitulos) {
        taSegundoListado.setText("");
        Query q;
        if (capitulos == -1) {
            String query = "Anime.findByNombreCointaining&CapitulosMax";
            q = session.createNamedQuery(query);
            q.setParameter("nombre", nombre);
            q.setParameter("capitulos", Integer.MAX_VALUE);
        } else {
            String query = "Anime.findByNombreCointaining&CapitulosMax";
            q = session.createNamedQuery(query);
            q.setParameter("nombre", nombre);
            q.setParameter("capitulos", capitulos);
        }

        if (q.list().isEmpty()) {
            taSegundoListado.setText("No se encontraron coincidencias.");
            return;
        }

        Iterator it = q.list().iterator();
        while (it.hasNext()) {
            Anime a = (Anime) it.next();
            taSegundoListado.append(a.getNombre() + " | " + a.getCapTotales());
            if (it.hasNext()) {
                taSegundoListado.append("\n");
            }
        }
    }

    public void darAltaAnime(Session session, Anime a) {
        session.save(a);
    }

    public void modificarAnime(Session session, Anime a) {
        session.evict(a);
        session.update(a);
    }

    public void modificarNombreAnime(Session session, Anime a, String nuevoNombre) {
        a.setNombre(nuevoNombre);
        session.evict(a);
        session.update(a);
    }

    public void darBajaAnime(Session session, Anime a) {
        session.delete(a);
    }

}
