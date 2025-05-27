/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package proyecto.nimesuki.controlador;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
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
import proyecto.nimesuki.modelo.Favoritos;
import proyecto.nimesuki.modelo.FavoritosId;
import proyecto.nimesuki.servicio.FavoritosService;

/**
 *
 * @author eloy.castro
 */
@ExtendWith(MockitoExtension.class)
class FavoritosControllerTest {

    public FavoritosControllerTest() {
    }

    @Mock
    private FavoritosService favoritosService;

    @InjectMocks
    private FavoritosController favoritosController;

    private String ip = "127.0.0.1";
    private String user = "root";
    private String pass = "root";

    /**
     * Test of getAll method, of class FavoritosController.
     */
    @Test
    public void testGetAll() {
        FavoritosId id = new FavoritosId(1, 1);
        Favoritos favorito = new Favoritos(id, null, null, 5, 10);

        when(favoritosService.findAll(ip, user, pass)).thenReturn(List.of(favorito));

        List<Favoritos> result = favoritosController.getAll(ip, user, pass);

        assertEquals(1, result.size());
        assertEquals(favorito, result.get(0));
    }

    /**
     * Test of getByUserAnime method, of class FavoritosController.
     */
    @Test
    public void testGetByUserAnime() {
        FavoritosId id = new FavoritosId(1, 1);
        Favoritos favorito = new Favoritos(id, null, null, 5, 10);

        when(favoritosService.findByUsuarioAndAnime(ip, user, pass, "Eloy", "One Piece"))
                .thenReturn(List.of(favorito));

        ResponseEntity<List<Favoritos>> response
                = favoritosController.getByUserAnime(ip, user, pass, "Eloy", "One Piece");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(favorito, response.getBody().get(0));
    }

    /**
     * Test of getByUser method, of class FavoritosController.
     */
    @Test
    public void testGetByUser() {
        FavoritosId id = new FavoritosId(1, 1);
        Favoritos favorito = new Favoritos(id, null, null, 5, 10);

        when(favoritosService.findByUsuario(ip, user, pass, "Eloy")).thenReturn(List.of(favorito));

        ResponseEntity<List<Favoritos>> response
                = favoritosController.getByUser(ip, user, pass, "Eloy");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(favorito, response.getBody().get(0));
    }

    /**
     * Test of createFavorito method, of class FavoritosController.
     */
    @Test
    public void testCreateFavorito() {
        FavoritosId id = new FavoritosId(1, 1);
        Favoritos favorito = new Favoritos(id, null, null, 5, 10);

        when(favoritosService.save(ip, user, pass, favorito)).thenReturn(favorito);

        Favoritos result = favoritosController.createFavorito(ip, user, pass, favorito);

        assertEquals(favorito, result);
    }

    /**
     * Test of updateFavorito method, of class FavoritosController.
     */
    @Test
    public void testUpdateFavorito() {
        FavoritosId id = new FavoritosId(1, 1);
        Favoritos favorito = new Favoritos(id, null, null, 4, 12);

        when(favoritosService.update(ip, user, pass, favorito)).thenReturn(favorito);

        Favoritos result = favoritosController.updateFavorito(ip, user, pass, favorito);

        assertEquals(favorito, result);
    }

    /**
     * Test of deleteFavorito method, of class FavoritosController.
     */
    @Test
    public void testDeleteFavorito() {
        FavoritosId id = new FavoritosId(1, 1);

        doNothing().when(favoritosService).delete(ip, user, pass, id);

        assertDoesNotThrow(() -> favoritosController.deleteFavorito(ip, user, pass, id));
        verify(favoritosService, times(1)).delete(ip, user, pass, id);
    }

}
