package com.example.nimesukiapp.vista.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.vista.fragments.LoginFragment;
import com.example.nimesukiapp.vista.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VistaPerfil extends AppCompatActivity {
    private String nombreUsuarioLogueado = "";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        bottomNavigationView = findViewById(R.id.bottomNavigationView_perfil);

        if (savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);

            if (nombreUsuarioLogueado != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_perfil, new ProfileFragment())
                        .commit();

                bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                bottomNavigationView.setVisibility(VISIBLE);
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_perfil, new LoginFragment())
                        .commit();
                bottomNavigationView.setVisibility(INVISIBLE);
            }
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_catalog) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
                Intent intent = new Intent(this, AnimeRandomView.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);
                startActivity(intent, options.toBundle());
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
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
}
