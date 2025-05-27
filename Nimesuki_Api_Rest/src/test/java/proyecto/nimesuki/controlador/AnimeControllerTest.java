/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package proyecto.nimesuki.controlador;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import proyecto.nimesuki.modelo.Anime;
import proyecto.nimesuki.servicio.AnimeService;

/**
 *
 * @author eloy.castro
 */
@ExtendWith(MockitoExtension.class)
public class AnimeControllerTest {

    public AnimeControllerTest() {
    }

    @Mock
    private AnimeService animeService;

    @InjectMocks
    private AnimeController animeController;
    
    private String ip = "127.0.0.1";
    private String user = "root";
    private String pass = "root";

    /**
     * Test of getAll method, of class AnimeController.
     */
    @Test
    public void testGetAll() {
        List<Anime> mockAnimes = List.of(new Anime(1, "Re:Zero kara Hajimeru Isekai Seikatsu", "Acción, Fantasía, Isekai, Drama, Psicológico, Sobrenatural, Romance, Aventura", 2016, "Subaru Natsuki es un estudiante corriente de preparatoria que conoce a una hermosa chica de pelo plateado de otro mundo que lo rescata. Para devolverle el favor decide quedarse con ella, pero el destino con el que carga la muchacha es mucho más pesado de lo que Subaru puede imaginar. Los enemigos atacan sin descanso, uno tras otro, hasta que finalmente mueren tanto él como la chica. Subaru no quiere que la chica resulte herida, con lo que jura acabar con cualquier enemigo, con cualquier destino que le aguarde, siempre por protegerla.", "https://raw.githubusercontent.com/Eloycorme3/anime-imagenes/main/re_zero_t1.jpg", 25), new Anime(2, "Re:Zero kara Hajimeru Isekai Seikatsu: Memory Snow", "Acción, Fantasía, Isekai, Drama, Psicológico, Sobrenatural, Romance, Aventura", 2018, "Después de los intensos eventos ocurridos en la mansión de Roswaal, Subaru finalmente obtiene un merecido descanso y porfin podrá tener su cita con Emilia. Sin embargo, esta no podrá realizarse ya que Pack tiene su temporada de hatsumaki, A causa de eso, Subaru se le ocurre una idea: ¡hacer un festival de nieve en la mansión Roswaal!", "imagenes/main/re_zero_memory_snow.jpg", 1));

        when(animeService.findAll(ip, user, pass)).thenReturn(mockAnimes);

        List<Anime> result = animeController.getAll(ip, user, pass);

        assertEquals(2, result.size());
        assertEquals("Re:Zero kara Hajimeru Isekai Seikatsu", result.get(0).getNombre());
    }

    /**
     * Test of getByAnime method, of class AnimeController.
     */
    @Test
    public void testGetByAnime() {
        List<Anime> mockAnimes = List.of(new Anime(1, "Re:Zero kara Hajimeru Isekai Seikatsu", "Acción, Fantasía, Isekai, Drama, Psicológico, Sobrenatural, Romance, Aventura", 2016, "Subaru Natsuki es un estudiante corriente de preparatoria que conoce a una hermosa chica de pelo plateado de otro mundo que lo rescata. Para devolverle el favor decide quedarse con ella, pero el destino con el que carga la muchacha es mucho más pesado de lo que Subaru puede imaginar. Los enemigos atacan sin descanso, uno tras otro, hasta que finalmente mueren tanto él como la chica. Subaru no quiere que la chica resulte herida, con lo que jura acabar con cualquier enemigo, con cualquier destino que le aguarde, siempre por protegerla.", "https://raw.githubusercontent.com/Eloycorme3/anime-imagenes/main/re_zero_t1.jpg", 25));
        when(animeService.findByNombreContaining(ip, user, pass, "Re:Zero kara Hajimeru Isekai Seikatsu")).thenReturn(mockAnimes);

        ResponseEntity<List<Anime>> response = animeController.getByAnime(ip, user, pass, "Re:Zero kara Hajimeru Isekai Seikatsu");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Re:Zero kara Hajimeru Isekai Seikatsu", response.getBody().get(0).getNombre());
    }

    /**
     * Test of getRandomAnimeNotInFavoritos method, of class AnimeController.
     */
    @Test
    void testGetRandomAnimeNotInFavoritos() {
        String nombreUsuario = "Eloy";

        Anime anime1 = new Anime(1, "One Piece", "Acción, Aventura, Shounen, Fantasía, Comedia, Superpoderes, Supervivencia, Drama, Artes marciales", 1999, "Monkey D. Luffy, un joven con el sueño de convertirse en el Rey de los Piratas, zarpa en busca del legendario tesoro conocido como el \"One Piece\". A lo largo de su viaje, Luffy recluta a varios compañeros con habilidades únicas, enfrentando peligrosos enemigos y descubriendo secretos ocultos en un mundo lleno de islas misteriosas, mares tempestuosos y aventuras emocionantes. La lealtad, la amistad y la determinación son los pilares que guiarán a Luffy en su misión por conquistar el Grand Line.", "https://raw.githubusercontent.com/Eloycorme3/anime-imagenes/main/one_piece.jpg", 1126);

        Anime anime2 = new Anime(2, "Re:Zero kara Hajimeru Isekai Seikatsu", "Acción, Fantasía, Isekai, Drama, Psicológico, Sobrenatural, Romance, Aventura", 2016, "Subaru Natsuki es un estudiante corriente de preparatoria que conoce a una hermosa chica de pelo plateado de otro mundo que lo rescata. Para devolverle el favor decide quedarse con ella, pero el destino con el que carga la muchacha es mucho más pesado de lo que Subaru puede imaginar. Los enemigos atacan sin descanso, uno tras otro, hasta que finalmente mueren tanto él como la chica. Subaru no quiere que la chica resulte herida, con lo que jura acabar con cualquier enemigo, con cualquier destino que le aguarde, siempre por protegerla.", "https://raw.githubusercontent.com/Eloycorme3/anime-imagenes/main/re_zero_t1.jpg", 25);

        List<Anime> mockAnimes = List.of(anime1, anime2);

        when(animeService.findNotInFavoritosByUsuario(ip, user, pass, nombreUsuario))
                .thenReturn(mockAnimes);

        ResponseEntity<Anime> response = animeController.getRandomAnimeNotInFavoritos(ip, user, pass, nombreUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(mockAnimes.contains(response.getBody()));
    }

    /**
     * Test of create method, of class AnimeController.
     */
    @Test
    public void testCreate() {
        Anime anime = new Anime(null, "One Piece", "Acción, Aventura, Shounen, Fantasía, Comedia, Superpoderes, Supervivencia, Drama, Artes marciales", 1999, "Monkey D. Luffy, un joven con el sueño de convertirse en el Rey de los Piratas, zarpa en busca del legendario tesoro conocido como el \"One Piece\". A lo largo de su viaje, Luffy recluta a varios compañeros con habilidades únicas, enfrentando peligrosos enemigos y descubriendo secretos ocultos en un mundo lleno de islas misteriosas, mares tempestuosos y aventuras emocionantes. La lealtad, la amistad y la determinación son los pilares que guiarán a Luffy en su misión por conquistar el Grand Line.", "https://raw.githubusercontent.com/Eloycorme3/anime-imagenes/main/one_piece.jpg", 1126);
        Anime savedAnime = new Anime(1, "One Piece", "Acción, Aventura, Shounen, Fantasía, Comedia, Superpoderes, Supervivencia, Drama, Artes marciales", 1999, "Monkey D. Luffy, un joven con el sueño de convertirse en el Rey de los Piratas, zarpa en busca del legendario tesoro conocido como el \"One Piece\". A lo largo de su viaje, Luffy recluta a varios compañeros con habilidades únicas, enfrentando peligrosos enemigos y descubriendo secretos ocultos en un mundo lleno de islas misteriosas, mares tempestuosos y aventuras emocionantes. La lealtad, la amistad y la determinación son los pilares que guiarán a Luffy en su misión por conquistar el Grand Line.", "https://raw.githubusercontent.com/Eloycorme3/anime-imagenes/main/one_piece.jpg", 1126);

        when(animeService.save(ip, user, pass, anime)).thenReturn(savedAnime);

        Anime result = animeController.create(ip, user, pass, anime);

        assertEquals(1, result.getIdAnime());
        assertEquals("One Piece", result.getNombre());
    }

    /**
     * Test of update method, of class AnimeController.
     */
    @Test
    void testUpdate() {
        Anime animeToUpdate = new Anime(null, "One Piece", "Acción, Aventura, Shounen, Fantasía, Comedia, Superpoderes, Supervivencia, Drama, Artes marciales", 1999, "Monkey D. Luffy, un joven con el sueño de convertirse en el Rey de los Piratas, zarpa en busca del legendario tesoro conocido como el \"One Piece\". A lo largo de su viaje, Luffy recluta a varios compañeros con habilidades únicas, enfrentando peligrosos enemigos y descubriendo secretos ocultos en un mundo lleno de islas misteriosas, mares tempestuosos y aventuras emocionantes. La lealtad, la amistad y la determinación son los pilares que guiarán a Luffy en su misión por conquistar el Grand Line.", "https://raw.githubusercontent.com/Eloycorme3/anime-imagenes/main/one_piece.jpg", 1126);
        Anime updated = new Anime(1, "One Piece", "Acción, Aventura, Shounen, Fantasía, Comedia, Superpoderes, Supervivencia, Drama, Artes marciales", 1999, "Monkey D. Luffy, un joven con el sueño de convertirse en el Rey de los Piratas, zarpa en busca del legendario tesoro conocido como el \"One Piece\". A lo largo de su viaje, Luffy recluta a varios compañeros con habilidades únicas, enfrentando peligrosos enemigos y descubriendo secretos ocultos en un mundo lleno de islas misteriosas, mares tempestuosos y aventuras emocionantes. La lealtad, la amistad y la determinación son los pilares que guiarán a Luffy en su misión por conquistar el Grand Line.", "https://raw.githubusercontent.com/Eloycorme3/anime-imagenes/main/one_piece.jpg", 1126);

        when(animeService.findById(ip, user, pass, 1)).thenReturn(new Anime());
        when(animeService.update(ip, user, pass, 1, animeToUpdate)).thenReturn(updated);

        ResponseEntity<Anime> response = animeController.update(ip, user, pass, 1, animeToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("One Piece", response.getBody().getNombre());
    }

    /**
     * Test of delete method, of class AnimeController.
     */
    @Test
    void testDelete() {
        doNothing().when(animeService).delete(ip, user, pass, 1);

        assertDoesNotThrow(() -> animeController.delete(ip, user, pass, 1));
        verify(animeService).delete(ip, user, pass, 1);
    }

}
