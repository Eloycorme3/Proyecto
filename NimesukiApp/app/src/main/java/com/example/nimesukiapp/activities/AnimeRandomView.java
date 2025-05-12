package com.example.nimesukiapp.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.fragments.AnimeDetailFragment;
import com.example.nimesukiapp.fragments.AnimeRandomFragment;
import com.example.nimesukiapp.fragments.CatalogFragment;
import com.example.nimesukiapp.fragments.LoginFragment;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class AnimeRandomView extends AppCompatActivity implements CatalogFragment.OnAnimeSelectedListener {
    private String nombreUsuarioLogueado = "";
    private BottomNavigationView bottomNavigationView;
    private AnimeRandomFragment animeRandomFragment; // Referencia al fragmento de anime

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        bottomNavigationView = findViewById(R.id.bottomNavigationView_random);

        // Obtener el nombre de usuario guardado
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);

        // Verificar si el usuario está logueado
        if (nombreUsuarioLogueado != null) {
            obtenerYMostrarAnimeRandom(); // Obtener y mostrar un anime aleatorio
            bottomNavigationView.setSelectedItemId(R.id.nav_random);
            bottomNavigationView.setVisibility(VISIBLE);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_random, new LoginFragment())
                    .commit();
            bottomNavigationView.setVisibility(INVISIBLE);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_catalog) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_favorites) {
                Intent intent = new Intent(this, ListaAnimesFavoritosActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_random) {
                obtenerYMostrarAnimeRandom(); // Actualizar el anime al presionar el botón
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, VistaPerfil.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    // Función para obtener un anime aleatorio y mostrarlo
    private void obtenerYMostrarAnimeRandom() {
        // Mostrar progreso mientras se carga el anime
        new Thread(() -> {
            try {
                Anime animeAleatorio = obtenerAnimeRandom(nombreUsuarioLogueado);

                runOnUiThread(() -> {
                    // Si el fragmento ya está agregado, solo actualizamos el anime
                    if (animeRandomFragment == null) {
                        animeRandomFragment = new AnimeRandomFragment(animeAleatorio);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_random, animeRandomFragment)
                                .commit();
                    } else {
                        animeRandomFragment.actualizarAnime(animeAleatorio); // Actualizar el anime
                    }
                });
            } catch (IOException e) {
                e.printStackTrace(); // Manejo de errores en caso de que falle la conexión
            }
        }).start();
    }

    // Función para obtener un anime aleatorio desde el servidor
    private Anime obtenerAnimeRandom(String nombreUsuario) throws IOException {
        ServicioREST servicioREST = new ServicioREST();
        return servicioREST.obtenerAnimeNoFavorito(nombreUsuario);
    }

    // Función para inflar el menú
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public void onAnimeSelected(Anime anime) {
        AnimeDetailFragment animeDetailFragment = AnimeDetailFragment.newInstance(anime);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, animeDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
