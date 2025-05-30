package com.example.nimesukiapp.view.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.view.fragments.AnimeFavoritoDetailFragment;
import com.example.nimesukiapp.view.fragments.ListaAnimesFavoritosFragment;
import com.example.nimesukiapp.view.fragments.LoginFragment;
import com.example.nimesukiapp.model.vo.Favoritos;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ListaAnimesFavoritosView extends AppCompatActivity implements ListaAnimesFavoritosFragment.OnAnimeFavoriteSelectedListener {
    private String nombreUsuarioLogueado = "";
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_favoritos);
        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottomNavigationViewFavoritos);

        if (savedInstanceState == null) {
            nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);

            if (nombreUsuarioLogueado != null) {
                getSupportFragmentManager().beginTransaction()
                       .replace(R.id.fragmentContainerFavoritos, new ListaAnimesFavoritosFragment())
                       .commit();
                bottomNavigationView.setVisibility(VISIBLE);
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerFavoritos, new LoginFragment())
                        .commit();
                bottomNavigationView.setVisibility(INVISIBLE);
            }
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_catalog) {
                Intent intent = new Intent(this, ListaAnimesView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_favorites) {
                return true;
            } else if (itemId == R.id.nav_random) {
                Intent intent = new Intent(this, AnimeRandomView.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, 0, 0);
                startActivity(intent, options.toBundle());
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, PerfilView.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, 0, 0);
                startActivity(intent, options.toBundle());
                finish();
                return true;
            }

            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        bottomNavigationView.setSelectedItemId(R.id.nav_favorites);
        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public void onAnimeFavoritoSelected(Favoritos favorito) {
        AnimeFavoritoDetailFragment animeFavoritoDetailFragment = AnimeFavoritoDetailFragment.newInstance(
                favorito
        );

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_enter,
                R.anim.fragment_exit,
                R.anim.fragment_pop_enter,
                R.anim.fragment_pop_exit
        );
        transaction.replace(R.id.fragmentContainerFavoritos, animeFavoritoDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
