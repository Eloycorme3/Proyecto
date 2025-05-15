package com.example.nimesukiapp.vista.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.nimesukiapp.R;

public class NimesukiSplashActivity extends Activity {
    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        boolean splashAlreadyShown = prefs.getBoolean("splash_screen", false);

        if (splashAlreadyShown) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo);
        View shimmerView = findViewById(R.id.shimmer_view);

        Animation scaleFadeIn = AnimationUtils.loadAnimation(this, R.anim.scale_fade_in);
        Animation shimmerAnim = AnimationUtils.loadAnimation(this, R.anim.shimmer_anim);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        logo.startAnimation(scaleFadeIn);
        shimmerView.startAnimation(shimmerAnim);

        scaleFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.startAnimation(shake);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            prefs.edit().putBoolean("splash_screen", true).apply();

            startActivity(new Intent(NimesukiSplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DURATION);
    }
}