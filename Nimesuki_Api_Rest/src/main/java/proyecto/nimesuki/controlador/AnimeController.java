/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.controlador;

import java.util.List;
import java.util.Random;
import proyecto.nimesuki.modelo.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.nimesuki.repositorio.AnimeRepository;

/**
 *
 * @author eloy.castro
 */
@RestController
@RequestMapping("/animes")
public class AnimeController {

    @Autowired
    private AnimeRepository animeRepository;

    @GetMapping
    public List<Anime> getAll() {
        return animeRepository.findAll();
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<List<Anime>> getByAnime(@PathVariable String nombre) {
        if (nombre != null) {
            List<Anime> animesFiltrados = animeRepository.findByNombreContaining(nombre);
            return ResponseEntity.ok(animesFiltrados);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/no-favoritos/{nombreUsuario}")
    public ResponseEntity<Anime> getRandomAnimeNotInFavoritos(
            @PathVariable String nombreUsuario) {

        List<Anime> animes = animeRepository.findNotInFavoritosByUsuario(nombreUsuario);

        if (animes.isEmpty()) {
            return ResponseEntity.ok().body(null);
        }

        Anime randomAnime = animes.get(new Random().nextInt(animes.size()));
        return ResponseEntity.ok(randomAnime);
    }

    @PostMapping
    public Anime create(@RequestBody Anime anime) {
        return animeRepository.save(anime);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Anime> update(@PathVariable Integer id, @RequestBody Anime anime) {
        return animeRepository.findById(id)
                .map(a -> {
                    anime.setIdAnime(id);
                    return ResponseEntity.ok(animeRepository.save(anime));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        animeRepository.deleteById(id);
    }
}
