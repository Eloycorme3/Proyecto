/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.servicio;

import jakarta.persistence.EntityManager;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import proyecto.nimesuki.datasource.DynamicDataSourceFactory;
import proyecto.nimesuki.datasource.DynamicEntityManagerFactory;
import proyecto.nimesuki.modelo.Favoritos;
import proyecto.nimesuki.modelo.FavoritosId;
import proyecto.nimesuki.repositorio.FavoritosRepository;

/**
 *
 * @author eloy.castro
 */
@Service
public class FavoritosService {

    private final FavoritosRepository repo = new FavoritosRepository();

    private EntityManager createEM(String ip, String user, String pass) {
        DataSource ds = DynamicDataSourceFactory.create(ip, user, pass);
        String key = ip + "|" + user + "|" + pass;
        return DynamicEntityManagerFactory.createEntityManager(ds, key);
    }

    public List<Favoritos> findAll(String ip, String user, String pass) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.findAll(em);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Favoritos> findByUsuario(String ip, String user, String pass, String nombreUsuario) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.findByUsuarioNombre(em, nombreUsuario);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Favoritos> findByUsuarioAndAnime(String ip, String user, String pass, String nombreUsuario, String nombreAnime) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.findByUsuarioNombreAndAnimeNombre(em, nombreUsuario, nombreAnime);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public Favoritos save(String ip, String user, String pass, Favoritos favorito) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.save(em, favorito);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public void delete(String ip, String user, String pass, FavoritosId id) {
        EntityManager em = createEM(ip, user, pass);
        try {
            repo.deleteById(em, id);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public Favoritos update(String ip, String user, String pass, Favoritos favorito) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.update(em, favorito);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
