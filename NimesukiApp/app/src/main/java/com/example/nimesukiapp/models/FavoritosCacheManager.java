package com.example.nimesukiapp.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.nimesukiapp.models.vo.Favoritos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoritosCacheManager {
    private static final String PREFS_NAME = "MisPreferencias";
    private static final String KEY_FAVORITOS = "favoritos_cache";

    private SharedPreferences prefs;
    private Gson gson;

    public FavoritosCacheManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void guardarFavoritos(ArrayList<String> favoritos) {
        String json = gson.toJson(favoritos);
        prefs.edit().putString(KEY_FAVORITOS, json).apply();
    }

    public ArrayList<String> cargarFavoritos() {
        String json = prefs.getString(KEY_FAVORITOS, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}

