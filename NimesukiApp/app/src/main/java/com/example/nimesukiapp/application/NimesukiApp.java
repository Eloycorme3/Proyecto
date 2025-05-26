package com.example.nimesukiapp.application;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.nimesukiapp.notification.WeeklyAnimeNotificationReceiver;

import java.util.Calendar;

public class NimesukiApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        if (prefs.contains("oscuro")) {
            boolean modoOscuro = prefs.getBoolean("oscuro", false);
            int nightMode = modoOscuro ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
            AppCompatDelegate.setDefaultNightMode(nightMode);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

    }

}

