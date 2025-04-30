/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
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

}
