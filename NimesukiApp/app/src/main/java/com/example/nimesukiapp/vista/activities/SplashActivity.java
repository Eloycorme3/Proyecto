package com.example.nimesukiapp.vista.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.nimesukiapp.R;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View splashLayout = findViewById(R.id.splash_root);

        new Handler().postDelayed(() -> {
            splashLayout.animate()
                    .alpha(0f)
                    .setDuration(800)
                    .withEndAction(() -> {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    })
                    .start();
        }, 1500);
    }
}