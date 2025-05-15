package com.example.nimesukiapp.vista.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.example.nimesukiapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkMode = prefs.getBoolean("oscuro", false);

        setNightMode(darkMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 1500);
    }

    private void setNightMode(boolean isNightMode) {
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();

        int nightMode = isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;

        if (currentNightMode != nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode);
        }
    }
}
