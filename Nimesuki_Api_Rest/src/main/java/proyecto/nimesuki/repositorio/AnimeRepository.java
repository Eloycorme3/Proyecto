/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import proyecto.nimesuki.modelo.Anime;

/**
 *
 * @author eloy.castro
 */
public interface AnimeRepository extends JpaRepository<Anime, Integer> {

    public List<Anime> findByNombreContaining(String nombre);

    @Query("SELECT a FROM Anime a WHERE a.idAnime NOT IN "
            + "(SELECT f.id.anime FROM Favoritos f WHERE f.usuario.nombre = :nombreUsuario)")
    List<Anime> findNotInFavoritosByUsuario(@Param("nombreUsuario") String nombreUsuario);
}
