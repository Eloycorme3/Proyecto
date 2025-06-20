package com.example.nimesukiapp.view.activities;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.vo.Usuario;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private boolean reinicio = false;
    private boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String nombre = prefs.getString("nombreUsuario", null);
        if (nombre != null && !nombre.isEmpty()) {
            comprobarUsuario(nombre);
        } else {
            reinicio = true;
            error = false;
            continuarSplash();
        }
    }

    private void continuarSplash() {
        runOnUiThread(() -> {
            View splashLayout = findViewById(R.id.splash_root);

            new Handler(Looper.getMainLooper()).postDelayed(() -> splashLayout.animate()
                    .alpha(0f)
                    .setDuration(800)
                    .withEndAction(() -> {
                        splashLayout.setVisibility(GONE);
                        Intent intent = new Intent(this, ListaAnimesView.class);
                        intent.putExtra("reinicio", reinicio);
                        intent.putExtra("error", error);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    })
                    .start(), 1500);
        });
    }

    private void comprobarUsuario(String nombre) {
        ServicioREST servicioREST = new ServicioREST(getBaseContext());
        runOnUiThread(() -> servicioREST.obtenerUsuarioPorNombre(nombre, new ServicioREST.OnUsuarioObtenidoListener() {
            @Override
            public void onSuccess(Usuario usuario) {
                reinicio = false;
                error = false;
                continuarSplash();
            }

            @Override
            public void onError(Exception e) {
                reinicio = true;
                error = true;
                continuarSplash();
            }
        }));
    }
}