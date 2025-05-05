package com.example.nimesukiapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.fragments.AnimeDetailFragment;
import com.example.nimesukiapp.fragments.CatalogFragment;
import com.example.nimesukiapp.fragments.LoginFragment;
import com.example.nimesukiapp.models.vo.Anime;

public class MainActivity extends AppCompatActivity implements CatalogFragment.OnAnimeSelectedListener {

    private String nombreUSuarioLogueado = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            String nombreUsuario = prefs.getString("nombreUsuario", null);

            if (nombreUsuario != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CatalogFragment())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
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
            return true;
        } else if (item.getItemId() == R.id.nav_favorites) {
            /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent); A favoritos*/
            return true;
        } else if (item.getItemId() == R.id.nav_random) {
            /*Intent intent = new Intent(MainActivity.this, ListadoActoresActivity.class);
            startActivity(intent); A random*/
            return true;
        } else if (item.getItemId() == R.id.nav_profile) {
            /*Intent intent = new Intent(MainActivity.this, ListadoActoresActivity.class);
            startActivity(intent); A perfil*/
            return true;
        }

        return false;
    }

    @Override
    public void onAnimeSelected(Anime anime) {
        AnimeDetailFragment animeDetailFragment = AnimeDetailFragment.newInstance(
                anime
        );

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, animeDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}