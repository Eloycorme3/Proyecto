package com.example.nimesukiapp.view.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.FavoritosManager;
import com.example.nimesukiapp.model.vo.Favoritos;
import com.example.nimesukiapp.model.vo.Usuario;
import com.example.nimesukiapp.notification.WeeklyAnimeNotificationReceiver;
import com.example.nimesukiapp.view.fragments.AnimeDetailFragment;
import com.example.nimesukiapp.view.fragments.AnimeFavoritoDetailFragment;
import com.example.nimesukiapp.view.fragments.ListaAnimesFragment;
import com.example.nimesukiapp.view.fragments.LoginFragment;
import com.example.nimesukiapp.model.vo.Anime;
import com.example.nimesukiapp.view.fragments.NoConnectionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ListaAnimesView extends AppCompatActivity implements NoConnectionFragment.OnNetworkAvailableSuccessListener, ListaAnimesFragment.OnAnimeSelectedListener, LoginFragment.OnLoginSuccessListener, LoginFragment.OnRegisterSuccessListener {
    private String nombreUsuarioLogueado = null;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences prefs;
    private FavoritosManager cacheManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        cacheManager = new FavoritosManager(getBaseContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        requestNotificationPermission();

        scheduleWeeklyAnimeNotification(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationViewAnimes);
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
                if (isNetworkAvailable(getBaseContext())) {
                    prefs.edit().putBoolean("login", false).apply();
                    bottomNavigationView.setVisibility(VISIBLE);
                    if (isActivityActive() && savedInstanceState == null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerLista, new ListaAnimesFragment())
                                .commit();
                    }
                } else {
                    bottomNavigationView.setVisibility(INVISIBLE);
                    if (isActivityActive() && savedInstanceState == null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerLista, new NoConnectionFragment())
                                .commit();
                    }
                }
            } else {
                if (isNetworkAvailable(getBaseContext())) {
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

                    prefs.edit().putBoolean("login", true).apply();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerLista, new LoginFragment())
                            .commit();
                } else {
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

                    bottomNavigationView.setVisibility(INVISIBLE);
                    if (isActivityActive()) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerLista, new NoConnectionFragment())
                                .commit();
                    }
                }
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

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        1001);
            }
        }
    }

    public static void scheduleWeeklyAnimeNotification(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        boolean alreadyScheduled = prefs.getBoolean("alarm_scheduled", false);

        if (alreadyScheduled) return;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, WeeklyAnimeNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 1, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
        );

        prefs.edit().putBoolean("alarm_scheduled", true).apply();
    }

    private boolean isActivityActive() {
        return !isFinishing() && !isDestroyed();
    }

    private void iniciarlizarCache() {
        ServicioREST servicioREST = new ServicioREST(getBaseContext());

        new Thread(() -> servicioREST.obtenerFavoritosPorUsuario(nombreUsuarioLogueado, new ServicioREST.OnAnimesFavoritosObtenidosListener() {
            @Override
            public void onSuccess(ArrayList<Favoritos> favoritos) {
                if (favoritos != null) {
                    ArrayList<String> nombres = new ArrayList<>();
                    for (Favoritos f : favoritos) {
                        nombres.add(f.getAnime().getNombre());
                    }
                    cacheManager.guardarFavoritos(nombres);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("favoritosCargados", true);
                    editor.apply();

                    Intent intent = new Intent(getBaseContext(), ListaAnimesView.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ActivityOptions options = ActivityOptions
                            .makeCustomAnimation(getBaseContext(), 0, 0);
                    intent.putExtra("reinicio", false);
                    startActivity(intent, options.toBundle());
                }
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
        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false;

            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            );
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

    @Override
    public void onAnimeSelected(Anime anime) {
        ServicioREST servicioREST = new ServicioREST(getBaseContext());

        new Thread(() -> servicioREST.obtenerFavoritosPorId(
                nombreUsuarioLogueado,
                anime.getIdAnime(),
                new ServicioREST.OnAnimesFavoritosObtenidosPorIdListener() {

                    @Override
                    public void onSuccess(Favoritos f) {
                        runOnUiThread(() -> {
                            if (f != null) {
                                AnimeFavoritoDetailFragment animeFavoritoDetailFragment = AnimeFavoritoDetailFragment.newInstance(f);

                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .setCustomAnimations(
                                                R.anim.fragment_enter,
                                                R.anim.fragment_exit,
                                                R.anim.fragment_pop_enter,
                                                R.anim.fragment_pop_exit
                                        )
                                        .replace(R.id.fragmentContainerLista, animeFavoritoDetailFragment)
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
                                        .replace(R.id.fragmentContainerLista, animeDetailFragment)
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
                                    .replace(R.id.fragmentContainerLista, animeDetailFragment)
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
                Intent intent = new Intent(this, ListaAnimesView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ActivityOptions options = ActivityOptions
                        .makeCustomAnimation(this, 0, 0);
                intent.putExtra("reinicio", false);
                startActivity(intent, options.toBundle());
            } else {
                iniciarlizarCache();
            }

            bottomNavigationView.setVisibility(VISIBLE);
        });
    }

    @Override
    public void onRegisterSuccess(Usuario usuario) {
        runOnUiThread(() -> {
            guardarUsuarioEnPreferencias(usuario);

            Intent intent = new Intent(this, ListaAnimesView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityOptions options = ActivityOptions
                    .makeCustomAnimation(this, 0, 0);
            intent.putExtra("reinicio", false);
            startActivity(intent, options.toBundle());
            bottomNavigationView.setVisibility(VISIBLE);
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

    @Override
    public void onNetworkAvailableSuccess() {
        bottomNavigationView.setVisibility(VISIBLE);
    }
}