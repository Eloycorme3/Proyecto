/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package proyecto.nimesuki.controlador;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import proyecto.nimesuki.modelo.Usuario;
import proyecto.nimesuki.servicio.UsuarioService;

/**
 *
 * @author eloy.castro
 */
@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    public UsuarioControllerTest() {
    }

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private final String ip = "127.0.0.1";
    private final String user = "root";
    private final String pass = "root";

    /**
     * Test of getAll method, of class UsuarioController.
     */
    @Test
    public void testGetAll() {
        Usuario usuario = new Usuario(1, "Eloy", "contrasenhaHasheada");

        when(usuarioService.findAll(ip, user, pass)).thenReturn(List.of(usuario));

        List<Usuario> result = usuarioController.getAll(ip, user, pass);

        assertEquals(1, result.size());
        assertEquals(usuario, result.get(0));
    }

    /**
     * Test of getByNombre method, of class UsuarioController.
     */
    @Test
    public void testGetByNombre() {
        Usuario usuario = new Usuario(1, "Eloy", "contrasenhaHasheada");

        when(usuarioService.findByNombre(ip, user, pass, "Eloy")).thenReturn(List.of(usuario));

        ResponseEntity<List<Usuario>> response = usuarioController.getByNombre(ip, user, pass, "Eloy");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(usuario, response.getBody().get(0));
    }

    /**
     * Test of create method, of class UsuarioController.
     */
    @Test
    public void testCreate() {
        Usuario usuario = new Usuario(1, "Eloy", "contrasenhaHasheada");

        when(usuarioService.save(ip, user, pass, usuario)).thenReturn(usuario);

        Usuario result = usuarioController.create(ip, user, pass, usuario);

        assertEquals(usuario, result);
    }

    /**
     * Test of update method, of class UsuarioController.
     */
    @Test
    public void testUpdate() {
        Usuario usuario = new Usuario(1, "Eloy", "contrasenhaHasheada");
        Usuario updatedUsuario = new Usuario(1, "Eloy", "nuevaContrasenhaHasheada");

        when(usuarioService.findById(ip, user, pass, 1)).thenReturn(usuario);
        when(usuarioService.update(ip, user, pass, 1, updatedUsuario)).thenReturn(updatedUsuario);

        ResponseEntity<Usuario> response = usuarioController.update(ip, user, pass, 1, updatedUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUsuario, response.getBody());
    }

    /**
     * Test of delete method, of class UsuarioController.
     */
    @Test
    public void testDelete() {
        doNothing().when(usuarioService).delete(ip, user, pass, 1);

        assertDoesNotThrow(() -> usuarioController.delete(ip, user, pass, 1));
        verify(usuarioService, times(1)).delete(ip, user, pass, 1);
    }

}
