package com.example.nimesukiapp.application;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class NimesukiApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        prefs.edit().putBoolean("inicio", true).apply();

        if (prefs.contains("oscuro")) {
            boolean modoOscuro = prefs.getBoolean("oscuro", false);
            int nightMode = modoOscuro ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
            AppCompatDelegate.setDefaultNightMode(nightMode);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

    }

}

