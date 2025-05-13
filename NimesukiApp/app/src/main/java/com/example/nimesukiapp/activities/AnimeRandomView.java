package com.example.nimesukiapp.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

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
    private AnimeRandomFragment animeRandomFragment;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        bottomNavigationView = findViewById(R.id.bottomNavigationView_random);

        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);

        if (nombreUsuarioLogueado != null) {
            obtenerYMostrarAnimeRandom();
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
                obtenerYMostrarAnimeRandom();
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, VistaPerfil.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    private void obtenerYMostrarAnimeRandom() {
        new Thread(() -> {
            try {
                Anime animeAleatorio = obtenerAnimeRandom(nombreUsuarioLogueado);

                runOnUiThread(() -> {
                    if (animeRandomFragment == null) {
                        animeRandomFragment = new AnimeRandomFragment(animeAleatorio);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_random, animeRandomFragment)
                                .commit();
                    } else {
                        animeRandomFragment.actualizarAnime(animeAleatorio);
                    }
                });
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.load_anime_error), Toast.LENGTH_LONG).show();
            }
        }).start();
    }

    private Anime obtenerAnimeRandom(String nombreUsuario) throws IOException {
        ServicioREST servicioREST = new ServicioREST();
        return servicioREST.obtenerAnimeNoFavorito(nombreUsuario);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public void onAnimeSelected(Anime anime) {
        AnimeDetailFragment animeDetailFragment = AnimeDetailFragment.newInstance(
                anime
        );

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, animeDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
