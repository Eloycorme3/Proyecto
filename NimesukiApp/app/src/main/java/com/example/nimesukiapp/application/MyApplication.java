package com.example.nimesukiapp.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import java.util.Locale;

public class MyApplication extends Application {
    public static final String ACTION_LANGUAGE_CHANGED = "com.example.nimesukiapp.LANGUAGE_CHANGED";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String idiomaGuardado = prefs.getString("idioma", "es");  // Idioma por defecto

        cambiarIdioma(idiomaGuardado);

        Toast.makeText(this, "Idioma cambiado a " + idiomaGuardado, Toast.LENGTH_SHORT).show();



        Intent intent = new Intent(ACTION_LANGUAGE_CHANGED);
        sendBroadcast(intent);
    }

    private void cambiarIdioma(String idioma) {
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);

        // Crear la configuración para el idioma
        Configuration configuration = getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(new LocaleList(locale));  // Para versiones modernas
        } else {
            configuration.setLocale(locale);  // Para versiones antiguas
        }

        // Cambiar la configuración del contexto global
        Context context = createConfigurationContext(configuration);
        Resources resources = context.getResources();

        // Aplicar la nueva configuración de idioma
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }



}
