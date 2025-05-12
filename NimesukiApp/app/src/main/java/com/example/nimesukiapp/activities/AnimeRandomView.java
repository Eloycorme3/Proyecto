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

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.fragments.AnimeRandomFragment;
import com.example.nimesukiapp.fragments.CatalogFavoritesFragment;
import com.example.nimesukiapp.fragments.LoginFragment;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;

public class AnimeRandomView extends AppCompatActivity {
    private String nombreUsuarioLogueado = "";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        bottomNavigationView = findViewById(R.id.bottomNavigationView_random);

        if (savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);

            if (nombreUsuarioLogueado != null) {
                Anime a = null;
                try {
                    a = obtenerAnimeRandom(nombreUsuarioLogueado);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                boolean fromCatalog = getIntent().getBooleanExtra("fromCatalog", false);
                if (fromCatalog) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_random, new AnimeRandomFragment(a))
                            .addToBackStack(null)
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_random, new AnimeRandomFragment(a))
                            .commit();
                }

                bottomNavigationView.setSelectedItemId(R.id.nav_random);
                bottomNavigationView.setVisibility(VISIBLE);
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_random, new LoginFragment())
                        .commit();
                bottomNavigationView.setVisibility(INVISIBLE);
            }
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
                Intent intent = new Intent(this, AnimeRandomView.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, VistaPerfil.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    private Anime obtenerAnimeRandom(String nombreUsuario) throws IOException {
        ServicioREST servicioREST = new ServicioREST();
        Anime a = servicioREST.obtenerAnimeNoFavorito(nombreUsuario);
        return a;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }
}
