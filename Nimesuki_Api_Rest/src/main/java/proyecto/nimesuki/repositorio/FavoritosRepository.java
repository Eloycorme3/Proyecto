/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.repositorio;

import jakarta.persistence.EntityManager;
import java.util.List;
import proyecto.nimesuki.modelo.Favoritos;
import proyecto.nimesuki.modelo.FavoritosId;

/**
 *
 * @author eloy.castro
 */
public class FavoritosRepository {

    public List<Favoritos> findAll(EntityManager em) {
        return em.createQuery("SELECT f FROM Favoritos f", Favoritos.class).getResultList();
    }

    public List<Favoritos> findByUsuarioNombre(EntityManager em, String nombreUsuario) {
        return em.createQuery(
                "SELECT f FROM Favoritos f WHERE f.usuario.nombre = :nombre", Favoritos.class)
                .setParameter("nombre", nombreUsuario)
                .getResultList();
    }

    public List<Favoritos> findByUsuarioNombreAndAnimeNombre(EntityManager em, String nombreUsuario, String nombreAnime) {
        return em.createQuery(
                "SELECT f FROM Favoritos f WHERE f.usuario.nombre = :usuario AND f.anime.nombre LIKE :anime", Favoritos.class)
                .setParameter("usuario", nombreUsuario)
                .setParameter("anime", "%" + nombreAnime + "%")
                .getResultList();
    }
    
    public Favoritos findByAnimeId(EntityManager em, String nombreUsuario, Integer id) {
        return em.createQuery(
                "SELECT f FROM Favoritos f WHERE f.usuario.nombre LIKE :nombreUsuario AND f.anime.idAnime = :id_anime", Favoritos.class)
                .setParameter("nombreUsuario", nombreUsuario)
                .setParameter("id_anime", id)
                .getResultList()
                .get(0);
    }

    public Favoritos save(EntityManager em, Favoritos favorito) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(favorito);
            tx.commit();
            return favorito;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public Favoritos update(EntityManager em, Favoritos favorito) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            Favoritos merged = em.merge(favorito);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public void deleteById(EntityManager em, FavoritosId id) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            Favoritos f = em.find(Favoritos.class, id);
            if (f != null) {
                em.remove(f);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
