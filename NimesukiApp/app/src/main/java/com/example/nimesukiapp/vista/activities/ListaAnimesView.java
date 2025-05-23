package com.example.nimesukiapp.vista.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.models.FavoritosManager;
import com.example.nimesukiapp.models.vo.Favoritos;
import com.example.nimesukiapp.models.vo.Usuario;
import com.example.nimesukiapp.vista.fragments.AnimeDetailFragment;
import com.example.nimesukiapp.vista.fragments.AnimeFavoritoDetailFragment;
import com.example.nimesukiapp.vista.fragments.ListaAnimesFragment;
import com.example.nimesukiapp.vista.fragments.LoginFragment;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class ListaAnimesView extends AppCompatActivity implements ListaAnimesFragment.OnAnimeSelectedListener, LoginFragment.OnLoginSuccessListener, LoginFragment.OnRegisterSuccessListener {
    private String nombreUsuarioLogueado = "";
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences prefs;
    private FavoritosManager cacheManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        cacheManager = new FavoritosManager(getBaseContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        bottomNavigationView = findViewById(R.id.bottomNavigationView_main);
        nombreUsuarioLogueado = null;

        if (prefs.contains("nombreUsuario")) {
            nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);
        }

        if (savedInstanceState == null) {
            boolean reinicio = getIntent().getBooleanExtra("reinicio", false);
            if (!reinicio) {
                if (prefs.contains("idioma")) {
                    String idioma = prefs.getString("idioma", "es");
                    cambiarIdioma(idioma);
                } else {
                    Locale defaultLocale;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        defaultLocale = Resources.getSystem().getConfiguration().getLocales().get(0);
                    } else {
                        defaultLocale = Resources.getSystem().getConfiguration().locale;
                    }

                    Locale.setDefault(defaultLocale);
                    Configuration config = getResources().getConfiguration();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        config.setLocales(new LocaleList(defaultLocale));
                    } else {
                        config.setLocale(defaultLocale);
                    }

                    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                }

                bottomNavigationView.setVisibility(VISIBLE);
                if (isActivityActive() && savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_main, new ListaAnimesFragment())
                            .commit();
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
                prefs.edit().clear().apply();
                boolean error = getIntent().getBooleanExtra("error", false);
                if (error) {
                    Toast.makeText(this, getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
                }

                bottomNavigationView.setVisibility(INVISIBLE);
                Locale defaultLocale;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    defaultLocale = Resources.getSystem().getConfiguration().getLocales().get(0);
                } else {
                    defaultLocale = Resources.getSystem().getConfiguration().locale;
                }

                Locale.setDefault(defaultLocale);
                Configuration config = getResources().getConfiguration();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    config.setLocales(new LocaleList(defaultLocale));
                } else {
                    config.setLocale(defaultLocale);
                }

                getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, new LoginFragment())
                        .commit();
            }
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_catalog) {
                return true;
            } else if (itemId == R.id.nav_favorites) {
                Intent intent = new Intent(this, ListaAnimesFavoritosView.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, 0, 0);
                startActivity(intent, options.toBundle());
                return true;
            } else if (itemId == R.id.nav_random) {
                Intent intent = new Intent(this, AnimeRandomView.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, 0, 0);
                startActivity(intent, options.toBundle());
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, PerfilView.class);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, 0, 0);
                startActivity(intent, options.toBundle());
                return true;
            }

            return false;
        });
    }

    private boolean isActivityActive() {
        return !isFinishing() && !isDestroyed();
    }

    private void iniciarlizarCache() {
        ServicioREST servicioREST = new ServicioREST(getBaseContext());

        new Thread(() -> servicioREST.obtenerFavoritosPorUsuario(nombreUsuarioLogueado, new ServicioREST.OnAnimesFavoritosObtenidosListener() {
            @Override
            public void onSuccess(ArrayList<Favoritos> favoritos) {
                ArrayList<String> nombres = new ArrayList<>();
                for (Favoritos f : favoritos) {
                    nombres.add(f.getAnime().getNombre());
                }
                cacheManager.guardarFavoritos(nombres);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("favoritosCargados", true);
                Log.d("FAVS", "Guardando favoritos: " + nombres);
                editor.apply();

                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(0, 0)
                        .replace(R.id.fragment_container_main, new ListaAnimesFragment())
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Toast.makeText(getBaseContext(), getString(R.string.error_init_favorites), Toast.LENGTH_LONG).show());
            }

        })).start();
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
        ServicioREST servicioREST = new ServicioREST(getBaseContext());

        new Thread(() -> servicioREST.obtenerFavoritosPorNombre(
                nombreUsuarioLogueado,
                anime.getNombre(),
                new ServicioREST.OnAnimesFavoritosObtenidosPorNombreListener() {
                    @Override
                    public void onSuccess(final ArrayList<Favoritos> animesFavoritos) {
                        runOnUiThread(() -> {
                            if (!animesFavoritos.isEmpty()) {
                                Favoritos favorito = animesFavoritos.get(0);
                                AnimeFavoritoDetailFragment animeFavoritoDetailFragment = AnimeFavoritoDetailFragment.newInstance(favorito);

                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .setCustomAnimations(
                                                R.anim.fragment_enter,
                                                R.anim.fragment_exit,
                                                R.anim.fragment_pop_enter,
                                                R.anim.fragment_pop_exit
                                        )
                                        .replace(R.id.fragment_container_main, animeFavoritoDetailFragment)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                AnimeDetailFragment animeDetailFragment = AnimeDetailFragment.newInstance(anime);

                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .setCustomAnimations(
                                                R.anim.fragment_enter,
                                                R.anim.fragment_exit,
                                                R.anim.fragment_pop_enter,
                                                R.anim.fragment_pop_exit
                                        )
                                        .replace(R.id.fragment_container_main, animeDetailFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(() -> {
                            AnimeDetailFragment animeDetailFragment = AnimeDetailFragment.newInstance(anime);

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.fragment_enter,
                                            R.anim.fragment_exit,
                                            R.anim.fragment_pop_enter,
                                            R.anim.fragment_pop_exit
                                    )
                                    .replace(R.id.fragment_container_main, animeDetailFragment)
                                    .addToBackStack(null)
                                    .commit();
                        });
                    }
                })).start();
    }


    @Override
    public void onLoginSuccess(Usuario usuario) {
        runOnUiThread(() -> {
            guardarUsuarioEnPreferencias(usuario);

            boolean favoritosCargados = prefs.getBoolean("favoritosCargados", false);
            if (favoritosCargados) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, new ListaAnimesFragment())
                        .commit();
            } else {
                iniciarlizarCache();
            }

            bottomNavigationView.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onRegisterSuccess(Usuario usuario) {
        runOnUiThread(() -> {
            guardarUsuarioEnPreferencias(usuario);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_main, new ListaAnimesFragment())
                    .commit();

            bottomNavigationView.setVisibility(View.VISIBLE);
        });
    }

    public void guardarUsuarioEnPreferencias(Usuario usuario) {
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nombreUsuario", usuario.getNombre());
        Gson gson = new Gson();
        String usuarioJson = gson.toJson(usuario);
        editor.putString("usuario_completo", usuarioJson);
        editor.apply();

        nombreUsuarioLogueado = usuario.getNombre();
    }
}