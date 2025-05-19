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
import proyecto.nimesuki.modelo.Anime;
import proyecto.nimesuki.repositorio.AnimeRepository;

/**
 *
 * @author eloy.castro
 */
@Service
public class AnimeService {

    private final AnimeRepository repo = new AnimeRepository();

    private EntityManager createEM(String ip, String user, String pass) {
        DataSource ds = DynamicDataSourceFactory.create(ip, user, pass);
        String key = ip + "|" + user + "|" + pass;
        return DynamicEntityManagerFactory.createEntityManager(ds, key);
    }

    public List<Anime> findAll(String ip, String user, String pass) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.findAll(em);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Anime> findByNombreContaining(String ip, String user, String pass, String nombre) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.findByNombreContaining(em, nombre);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Anime> findNotInFavoritosByUsuario(String ip, String user, String pass, String nombreUsuario) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.findNotInFavoritosByUsuario(em, nombreUsuario);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Anime save(String ip, String user, String pass, Anime anime) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.save(em, anime);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Anime update(String ip, String user, String pass, Integer id, Anime anime) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.update(em, id, anime);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Anime findById(String ip, String user, String pass, Integer id) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.findById(em, id);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void delete(String ip, String user, String pass, Integer id) {
        EntityManager em = createEM(ip, user, pass);
        try {
            repo.deleteById(em, id);
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}