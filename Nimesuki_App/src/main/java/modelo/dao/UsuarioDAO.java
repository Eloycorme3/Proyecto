/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
import modelo.vo.Usuario;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author eloy.castro
 */
public class UsuarioDAO {

    public void cargarCombo(Session session, DefaultComboBoxModel modeloComboUsuarios) {
        modeloComboUsuarios.removeAllElements();
        Usuario u;

        String query = "Usuario.findAll";
        Query q = session.createNamedQuery(query);

        Iterator it = q.list().iterator();
        while (it.hasNext()) {
            u = (Usuario) it.next();
            modeloComboUsuarios.addElement(u);
        }
    }

    public void borrarUsuario(Session session, Usuario uCreado) {
        session.delete(uCreado);
    }

    public Usuario buscarUsuarioPorNombre(Session session, String nombreUsuario) {
        Usuario u = null;
        String query = "Usuario.findByNombre";
        Query q = session.createNamedQuery(query);
        q.setParameter("nombre", nombreUsuario);

        Iterator it = q.list().iterator();
        if (it.hasNext()) {
            u = (Usuario) it.next();
        }

        return u;
    }

    public void modificarNombreUsuario(Session session, Usuario uLogueado, String nuevoNombre) {
        uLogueado.setNombre(nuevoNombre);
        session.evict(uLogueado);
        session.update(uLogueado);
    }

    public void modificarContrasenhaUsuario(Session session, Usuario uLogueado, String nuevaContrasenha) {
        uLogueado.setContrasenha(nuevaContrasenha);
        session.evict(uLogueado);
        session.update(uLogueado);
    }

    public void guardarEloy(Session session, String eloy, String contrasenhaEncriptada, String tipo) {
        Usuario u = new Usuario(eloy, contrasenhaEncriptada, tipo);
        session.save(u);
    }

    public Usuario buscarUsuarioPorId(Session session, int id) {
        Usuario u = null;
        String query = "Usuario.findByIdUsuario";
        Query q = session.createNamedQuery(query);
        q.setParameter("idUsuario", id);
        
        Iterator it = q.list().iterator();
        if (it.hasNext()) {
            u = (Usuario) it.next();
        }
        return u;
    }

    public void darAltaUsuario(Session session, Usuario u) {
        session.save(u);
    }

    public void modificarUsuario(Session session, Usuario u) {
        session.evict(u);
        session.update(u);
    }

    public void modificarTipoUsuario(Session session, Usuario uLogAdmin, String tipo) {
        uLogAdmin.setTipo(tipo);
        session.evict(uLogAdmin);
        session.update(uLogAdmin);
    }

}
