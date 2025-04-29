/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.servicio;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.nimesuki.modelo.Favoritos;
import proyecto.nimesuki.repositorio.FavoritosRepository;

/**
 *
 * @author eloy.castro
 */
@Service
public class FavoritosService {

    @Autowired
    private FavoritosRepository favoritosRepository;
}

