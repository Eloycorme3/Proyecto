/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.repositorio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;
import proyecto.nimesuki.modelo.Anime;

/**
 *
 * @author eloy.castro
 */
public class AnimeRepository {

    public List<Anime> findAll(EntityManager em) {
        return em.createQuery("SELECT a FROM Anime a", Anime.class).getResultList();
    }

    public List<Anime> findByNombreContaining(EntityManager em, String nombre) {
        TypedQuery<Anime> query = em.createQuery(
                "SELECT a FROM Anime a WHERE a.nombre LIKE :nombre", Anime.class);
        query.setParameter("nombre", "%" + nombre + "%");
        return query.getResultList();
    }

    public List<Anime> findNotInFavoritosByUsuario(EntityManager em, String nombreUsuario) {
        return em.createQuery(
                "SELECT a FROM Anime a WHERE a.idAnime NOT IN "
                + "(SELECT f.anime.idAnime FROM Favoritos f WHERE f.usuario.nombre = :nombreUsuario)",
                Anime.class)
                .setParameter("nombreUsuario", nombreUsuario)
                .getResultList();
    }

    public Anime save(EntityManager em, Anime anime) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(anime);
            tx.commit();
            return anime;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public Anime findById(EntityManager em, Integer id) {
        return em.find(Anime.class, id);
    }

    public void deleteById(EntityManager em, Integer id) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            Anime a = em.find(Anime.class, id);
            if (a != null) {
                Query q = em.createQuery("DELETE FROM Favoritos f WHERE f.anime.idAnime = :idAnime");
                q.setParameter("idAnime", id).executeUpdate();
                em.remove(a);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public Anime update(EntityManager em, Integer id, Anime anime) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            anime.setIdAnime(id);
            Anime updated = em.merge(anime);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
