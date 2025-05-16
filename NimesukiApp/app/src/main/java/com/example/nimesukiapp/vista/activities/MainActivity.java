package com.example.nimesukiapp.vista.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.vista.fragments.AnimeDetailFragment;
import com.example.nimesukiapp.vista.fragments.CatalogFragment;
import com.example.nimesukiapp.vista.fragments.LoginFragment;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CatalogFragment.OnAnimeSelectedListener {
    private String nombreUsuarioLogueado = "";
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView_main);

        if (prefs.contains("nombreUsuario")) {
            if (prefs.contains("oscuro")) {
                boolean modoOscuro = prefs.getBoolean("oscuro", false);
                setNightMode(modoOscuro);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
            String idioma = prefs.getString("idioma", "es");
            cambiarIdioma(idioma);
            nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);
        }

        if (nombreUsuarioLogueado != null && !nombreUsuarioLogueado.isEmpty()) {
            bottomNavigationView.setVisibility(VISIBLE);
        } else {
            bottomNavigationView.setVisibility(INVISIBLE);
        }

        if (savedInstanceState == null) {
            if (nombreUsuarioLogueado != null && !nombreUsuarioLogueado.isEmpty()) {
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
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent, options.toBundle());
                return true;
            } else if (itemId == R.id.nav_random) {
                Intent intent = new Intent(MainActivity.this, AnimeRandomView.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent, options.toBundle());
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(MainActivity.this, VistaPerfil.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent, options.toBundle());
                return true;
            }

            return false;
        });
    }

    private void cambiarIdioma(String idioma) {
        Locale currentLocale = getResources().getConfiguration().locale;
        String idiomaActual = currentLocale.getLanguage();

        if (!idioma.equals(idiomaActual)) {
            Locale locale = new Locale(idioma);
            Locale.setDefault(locale);

            Configuration configuration = getResources().getConfiguration();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocales(new LocaleList(locale));
            } else {
                configuration.setLocale(locale);
            }

            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

            recreate();
        }
    }

    private void setNightMode(boolean isNightMode) {
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();

        int nightMode = isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;

        if (currentNightMode != nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        bottomNavigationView.setSelectedItemId(R.id.nav_catalog);
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
        transaction.setCustomAnimations(
                R.anim.fragment_enter,
                R.anim.fragment_exit,
                R.anim.fragment_pop_enter,
                R.anim.fragment_pop_exit
        );
        transaction.replace(R.id.fragment_container_main, animeDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
