package com.example.nimesukiapp.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.fragments.AnimeDetailFragment;
import com.example.nimesukiapp.fragments.CatalogFragment;
import com.example.nimesukiapp.fragments.LoginFragment;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements CatalogFragment.OnAnimeSelectedListener {

    private String nombreUsuarioLogueado = "";
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView_main);

        if (savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);

            if (nombreUsuarioLogueado != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, new CatalogFragment())
                        .commit();
                bottomNavigationView.setSelectedItemId(R.id.nav_catalog);
                bottomNavigationView.setVisibility(VISIBLE);
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, new LoginFragment())
                        .commit();
                bottomNavigationView.setVisibility(INVISIBLE);
            }
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_catalog) {
                return true;
            } else if (itemId == R.id.nav_favorites) {
                Intent intent = new Intent(MainActivity.this, ListaAnimesFavoritosActivity.class);
                intent.putExtra("fromCatalog", true);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_random) {
                Intent intent = new Intent(MainActivity.this, AnimeRandomView.class);
                intent.putExtra("fromCatalog", true);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(MainActivity.this, VistaPerfil.class);
                intent.putExtra("fromCatalog", true);
                startActivity(intent);
                return true;
            }

            return false;
        });
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
