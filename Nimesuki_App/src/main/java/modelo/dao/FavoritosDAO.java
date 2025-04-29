/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.util.Iterator;
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
    
}
