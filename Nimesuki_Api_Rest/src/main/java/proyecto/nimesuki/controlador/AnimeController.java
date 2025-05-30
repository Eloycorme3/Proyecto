/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.controlador;

import java.util.List;
import java.util.Random;
import proyecto.nimesuki.modelo.Anime;
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
import proyecto.nimesuki.servicio.AnimeService;

/**
 *
 * @author eloy.castro
 */
@RestController
@RequestMapping("/animes")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping
    public List<Anime> getAll(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass
    ) {
        return animeService.findAll(ip, user, pass);
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<List<Anime>> getByAnime(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @PathVariable String nombre
    ) {
        if (nombre != null) {
            List<Anime> animes = animeService.findByNombreContaining(ip, user, pass, nombre);
            return ResponseEntity.ok(animes);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/{id}")
    public ResponseEntity<Anime> getById(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @PathVariable Integer id
    ) {
        if (id != null) {
            Anime anime = animeService.findById(ip, user, pass, id);
            return ResponseEntity.ok(anime);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/no-favoritos/{nombreUsuario}")
    public ResponseEntity<Anime> getRandomAnimeNotInFavoritos(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @PathVariable String nombreUsuario
    ) {
        List<Anime> animes = animeService.findNotInFavoritosByUsuario(ip, user, pass, nombreUsuario);
        if (animes.isEmpty()) {
            return ResponseEntity.ok().body(null);
        }
        Anime randomAnime = animes.get(new Random().nextInt(animes.size()));
        return ResponseEntity.ok(randomAnime);
    }

    @PostMapping
    public Anime create(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @RequestBody Anime anime
    ) {
        return animeService.save(ip, user, pass, anime);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Anime> update(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @PathVariable Integer id,
            @RequestBody Anime anime
    ) {
        Anime a = animeService.findById(ip, user, pass, id);
        if (a != null) {
            return ResponseEntity.ok(animeService.update(ip, user, pass, id, anime));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public void delete(
            @RequestParam String ip,
            @RequestParam String user,
            @RequestParam String pass,
            @RequestBody Integer id
    ) {
        animeService.delete(ip, user, pass, id);
    }
}
