/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.nimesuki.modelo.Usuario;

/**
 *
 * @author eloy.castro
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public List<Usuario> findByNombre(String nombre);
}
