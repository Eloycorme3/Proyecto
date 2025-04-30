/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.controlador;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.nimesuki.modelo.Favoritos;
import proyecto.nimesuki.modelo.FavoritosId;
import proyecto.nimesuki.repositorio.FavoritosRepository;

/**
 *
 * @author eloy.castro
 */
@RestController
@RequestMapping("/favoritos")
public class FavoritosController {

    private final FavoritosRepository favoritosRepository;

    public FavoritosController(FavoritosRepository favoritosRepository) {
        this.favoritosRepository = favoritosRepository;
    }

    @GetMapping
    public List<Favoritos> getAll() {
        return favoritosRepository.findAll();
    }

    @GetMapping("/{idUsuario}/{nombre}")
    public ResponseEntity<List<Favoritos>> getByUserAnime(@PathVariable Integer idUsuario, @PathVariable String nombre) {
        if (nombre != null) {
            List<Favoritos> favoritosFiltrados = favoritosRepository.findByUsuario_IdUsuarioAndAnime_NombreContaining(idUsuario, nombre);
            
            return ResponseEntity.ok(favoritosFiltrados);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Favoritos>> getByUser(@PathVariable Integer idUsuario) {
        if (idUsuario != null) {
            List<Favoritos> favoritosFiltrados = favoritosRepository.findByUsuario_IdUsuario(idUsuario);
            
            return ResponseEntity.ok(favoritosFiltrados);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public Favoritos createFavorito(@RequestBody Favoritos favorito) {
        return favoritosRepository.save(favorito);
    }

    @PutMapping
    public Favoritos updateFavorito(@RequestBody Favoritos favorito) {
        return favoritosRepository.save(favorito);
    }

    @DeleteMapping
    public void deleteFavorito(@RequestBody FavoritosId id) {
        favoritosRepository.deleteById(id);
    }
}
