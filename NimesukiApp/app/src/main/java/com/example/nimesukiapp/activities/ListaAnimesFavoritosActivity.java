package com.example.nimesukiapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.fragments.AnimeDetailFragment;
import com.example.nimesukiapp.fragments.CatalogFavoritesFragment;
import com.example.nimesukiapp.fragments.CatalogFragment;
import com.example.nimesukiapp.fragments.LoginFragment;
import com.example.nimesukiapp.models.vo.Anime;
import com.example.nimesukiapp.models.vo.Favoritos;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ListaAnimesFavoritosActivity extends AppCompatActivity implements CatalogFavoritesFragment.OnAnimeFavoriteSelectedListener {
    private String nombreUSuarioLogueado = "";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_favoritos);

        bottomNavigationView = findViewById(R.id.bottomNavigationView_favoritos);

        if (savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            String nombreUsuario = prefs.getString("nombreUsuario", null);

            if (nombreUsuario != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_favoritos, new CatalogFragment())
                        .commit();
                bottomNavigationView.setSelectedItemId(R.id.nav_catalog);
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_favoritos, new LoginFragment())
                        .commit();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_catalog) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_favorites) {
            Intent intent = new Intent(this, ListaAnimesFavoritosActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_random) {
            Intent intent = new Intent(this, AnimeRandomView.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_profile) {
            Intent intent = new Intent(this, VistaPerfil.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
    @Override
    public void onAnimeFavoritoSelected(Favoritos favorito) {
        /*AnimeDetailFragment animeDetailFragment = AnimeDetailFragment.newInstance(
                favorito
        );

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, animeDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    */}
}
