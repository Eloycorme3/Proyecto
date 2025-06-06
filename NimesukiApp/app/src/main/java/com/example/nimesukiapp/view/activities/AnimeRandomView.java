package com.example.nimesukiapp.view.activities;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.view.fragments.AnimeRandomEmptyFragment;
import com.example.nimesukiapp.view.fragments.AnimeRandomFragment;
import com.example.nimesukiapp.view.fragments.LoginFragment;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.vo.Anime;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class AnimeRandomView extends AppCompatActivity {
    private String nombreUsuarioLogueado = null;
    private BottomNavigationView bottomNavigationView;
    private AnimeRandomFragment animeRandomFragment;
    private boolean bloquearVolver = false;
    private SharedPreferences prefs;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        loading = findViewById(R.id.progressBarLoadingRandom);
        mostrarProgress(true);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!bloquearVolver) {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationViewRandom);
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            View itemView = bottomNavigationView.findViewById(menuItem.getItemId());
            itemView.setOnLongClickListener(v -> true);
            itemView.setHapticFeedbackEnabled(false);
        }

        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);

        if (nombreUsuarioLogueado != null) {
            mostrarProgress(true);
            bloquearVolver = true;
            obtenerYMostrarAnimeRandom();
            bottomNavigationView.setVisibility(VISIBLE);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerRandom, new LoginFragment())
                    .commit();
            bottomNavigationView.setVisibility(INVISIBLE);
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
                Intent intent = new Intent(this, ListaAnimesFavoritosView.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, 0, 0);
                startActivity(intent, options.toBundle());
                finish();
                return true;
            } else if (itemId == R.id.nav_random) {
                mostrarProgress(true);
                bloquearVolver = true;
                obtenerYMostrarAnimeRandom();
                return true;
            } else if (itemId == R.id.nav_settings) {
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

    private void mostrarProgress(boolean mostrar) {
        loading.setVisibility(mostrar ? VISIBLE : GONE);
        findViewById(R.id.fragmentContainerRandom).setVisibility(mostrar ? INVISIBLE : VISIBLE);
    }

    public void obtenerYMostrarAnimeRandom() {
        new Thread(() -> {
            try {
                Anime animeAleatorio = obtenerAnimeRandom(nombreUsuarioLogueado);

                runOnUiThread(() -> {
                    if (isDestroyed() || getSupportFragmentManager().isStateSaved()) {
                        return;
                    }

                    if (animeAleatorio == null) {
                        if (!isFinishing()) {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragmentContainerRandom, new AnimeRandomEmptyFragment())
                                        .commit();
                                mostrarProgress(false);
                            }, 1000);
                            return;
                        }
                    }

                    animeRandomFragment = AnimeRandomFragment.newInstance(animeAleatorio);
                    enableBottomBar(false);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(0, 0)
                                .replace(R.id.fragmentContainerRandom, animeRandomFragment)
                                .commit();
                        mostrarProgress(false);
                        enableBottomBar(true);
                        bloquearVolver = false;
                    }, 1000);

                });
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.load_anime_error), Toast.LENGTH_LONG).show();
            }
        }).start();
    }

    private void enableBottomBar(boolean enable) {
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            bottomNavigationView.getMenu().getItem(i).setEnabled(enable);
        }
    }

    private Anime obtenerAnimeRandom(String nombreUsuario) throws IOException {
        ServicioREST servicioREST = new ServicioREST(getBaseContext());
        return servicioREST.obtenerAnimeNoFavorito(nombreUsuario);
    }

    @Override
    protected void onResume() {
        super.onResume();

        bottomNavigationView.setSelectedItemId(R.id.nav_random);
        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }
}
