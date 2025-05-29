/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.controlador;

import java.util.List;
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
import proyecto.nimesuki.modelo.Favoritos;
import proyecto.nimesuki.modelo.FavoritosId;
import proyecto.nimesuki.servicio.FavoritosService;

/**
 *
 * @author eloy.castro
 */
@RestController
@RequestMapping("/favoritos")
public class FavoritosController {

    private final FavoritosService favoritosService;

    public FavoritosController(FavoritosService favoritosService) {
        this.favoritosService = favoritosService;
    }

    @GetMapping
    public List<Favoritos> getAll(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass
    ) {
        return favoritosService.findAll(ip, user, pass);
    }

    @GetMapping("/{nombreUsuario}/{nombreAnime}")
    public ResponseEntity<List<Favoritos>> getByUserAnime(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @PathVariable String nombreUsuario,
            @PathVariable String nombreAnime
    ) {
        if (nombreUsuario != null && nombreAnime != null) {
            List<Favoritos> favoritos = favoritosService.findByUsuarioAndAnime(ip, user, pass, nombreUsuario, nombreAnime);
            return ResponseEntity.ok(favoritos);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{nombreUsuario}")
    public ResponseEntity<List<Favoritos>> getByUser(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @PathVariable String nombreUsuario
    ) {
        if (nombreUsuario != null) {
            List<Favoritos> favoritos = favoritosService.findByUsuario(ip, user, pass, nombreUsuario);
            return ResponseEntity.ok(favoritos);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/{nombreUsuario}/{idAnime}")
    public ResponseEntity<Favoritos> getByAnimeId(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @PathVariable String nombreUsuario,
            @PathVariable Integer idAnime
    ) {
        if (idAnime != null && !nombreUsuario.isEmpty()) {
            Favoritos f = favoritosService.findByAnimeId(ip, user, pass, nombreUsuario, idAnime);
            return ResponseEntity.ok(f);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public Favoritos createFavorito(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @RequestBody Favoritos favorito
    ) {
        return favoritosService.save(ip, user, pass, favorito);
    }

    @PutMapping
    public Favoritos updateFavorito(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @RequestBody Favoritos favorito
    ) {
        return favoritosService.update(ip, user, pass, favorito);
    }

    @DeleteMapping
    public void deleteFavorito(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @RequestBody FavoritosId id
    ) {
        favoritosService.delete(ip, user, pass, id);
    }
}
