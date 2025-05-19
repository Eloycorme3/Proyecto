/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.repositorio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import proyecto.nimesuki.modelo.Usuario;

/**
 *
 * @author eloy.castro
 */
public class UsuarioRepository {

    public List<Usuario> findAll(EntityManager em) {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    public List<Usuario> findByNombre(EntityManager em, String nombre) {
        TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nombre = :nombre", Usuario.class);
        query.setParameter("nombre", nombre);
        return query.getResultList();
    }

    public Usuario save(EntityManager em, Usuario usuario) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            Usuario merged = em.merge(usuario);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public Usuario update(EntityManager em, Integer id, Usuario usuario) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            usuario.setIdUsuario(id);
            Usuario merged = em.merge(usuario);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public void deleteById(EntityManager em, Integer id) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            Usuario u = em.find(Usuario.class, id);
            if (u != null) {
                em.remove(u);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public Usuario findById(EntityManager em, Integer id) {
        return em.find(Usuario.class, id);
    }
}
