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
import proyecto.nimesuki.modelo.Usuario;
import proyecto.nimesuki.repositorio.UsuarioRepository;

/**
 *
 * @author eloy.castro
 */
@Service
public class UsuarioService {

    private final UsuarioRepository repo = new UsuarioRepository();

    private EntityManager createEM(String ip, String user, String pass) {
        DataSource ds = DynamicDataSourceFactory.create(ip, user, pass);
        String key = ip + "_" + user + "_" + pass;  // clave única para cache
        return DynamicEntityManagerFactory.createEntityManager(ds, key);
    }

    public List<Usuario> findAll(String ip, String user, String pass) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.findAll(em);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Usuario> findByNombre(String ip, String user, String pass, String nombre) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.findByNombre(em, nombre);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public Usuario save(String ip, String user, String pass, Usuario usuario) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.save(em, usuario);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public Usuario update(String ip, String user, String pass, Integer id, Usuario usuario) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.update(em, id, usuario);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public void delete(String ip, String user, String pass, Integer id) {
        EntityManager em = createEM(ip, user, pass);
        try {
            repo.deleteById(em, id);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public Usuario findById(String ip, String user, String pass, Integer id) {
        EntityManager em = createEM(ip, user, pass);
        try {
            return repo.findById(em, id);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
