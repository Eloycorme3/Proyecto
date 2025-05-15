package com.example.nimesukiapp.vista.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.vista.fragments.AnimeRandomFragment;
import com.example.nimesukiapp.vista.fragments.LoginFragment;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class AnimeRandomView extends AppCompatActivity {
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
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);
                startActivity(intent, options.toBundle());
                return true;
            } else if (itemId == R.id.nav_favorites) {
                Intent intent = new Intent(this, ListaAnimesFavoritosActivity.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);
                startActivity(intent, options.toBundle());
                finish();
                return true;
            } else if (itemId == R.id.nav_random) {
                obtenerYMostrarAnimeRandom();
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, VistaPerfil.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent, options.toBundle());
                finish();
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
                    if (isDestroyed() || getSupportFragmentManager().isStateSaved()) {
                        return;
                    }

                    if (animeRandomFragment == null) {
                        animeRandomFragment = AnimeRandomFragment.newInstance(animeAleatorio);
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
}
