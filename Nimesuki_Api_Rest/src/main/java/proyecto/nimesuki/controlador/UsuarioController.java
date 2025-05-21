/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.controlador;

import java.util.List;
import proyecto.nimesuki.modelo.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import proyecto.nimesuki.servicio.UsuarioService;

/**
 *
 * @author eloy.castro
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> getAll(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass
    ) {
        return usuarioService.findAll(ip, user, pass);
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<List<Usuario>> getByNombre(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @PathVariable String nombre
    ) {
        if (nombre != null) {
            List<Usuario> usuarios = usuarioService.findByNombre(ip, user, pass, nombre);
            return ResponseEntity.ok(usuarios);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public Usuario create(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @RequestBody Usuario usuario
    ) {
        return usuarioService.save(ip, user, pass, usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @PathVariable Integer id,
            @RequestBody Usuario usuario
    ) {
        Usuario u = usuarioService.findById(ip, user, pass, id);
        if (u != null) {
            return ResponseEntity.ok(usuarioService.update(ip, user, pass, id, usuario));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @PathVariable Integer id
    ) {
        usuarioService.delete(ip, user, pass, id);
    }
}
