package com.example.nimesukiapp.mock;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nimesukiapp.models.vo.Anime;
import com.example.nimesukiapp.models.vo.Favoritos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServicioREST {
    String localhost = "127.0.0.1";
    public ArrayList<Anime> obtenerAnimes(OnPeliculasObtenidasListener listener) {
        ArrayList<Anime> listaAnimes = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + localhost + ":8088/animes")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Error", "Error al realizar la solicitud", e);
                listener.onError(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    listaAnimes.clear();
                    String responseData = response.body().string();
                    ArrayList<Anime> animesDevueltos;
                    Gson gson = new Gson();
                    animesDevueltos = gson.fromJson(responseData, new TypeToken<ArrayList<Anime>>() {
                    }.getType());
                    listaAnimes.addAll(animesDevueltos);
                    Log.d("OK", "Respuesta exitosa: " + responseData);
                    listener.onSuccess(animesDevueltos);
                } else {
                    Log.e("KO", "Respuesta no exitosa: " + response.code());
                    listener.onError(new IOException("Respuesta no exitosa: " + response.code()));
                }
            }
        });
        return listaAnimes;
    }

    public Anime obtenerPeliculaPorNombre(String nombre) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + localhost + ":8088/animes/" + nombre)
                .build();
        Anime a = null;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseData = response.body().string();
            if (response.isSuccessful() && response.body() != null) {
                Gson gson = new Gson();
                a = gson.fromJson(responseData, Anime.class);
                Log.d("OK", "Respuesta exitosa: " + responseData);
            } else {
                Log.e("KO", "Respuesta no exitosa: " + response.code());
            }
        }
        return a;
    }

    public ArrayList<Favoritos> obtenerFavoritosPorUsuario(String nombreUsuario) {
        ArrayList<Favoritos> listaFavoritos = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + localhost + ":8080/favoritos/" + nombreUsuario)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Error", "Error al realizar la solicitud", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    listaFavoritos.clear();
                    String responseData = response.body().string();
                    ArrayList<Favoritos> favoritosDevueltos;
                    Gson gson = new Gson();
                    favoritosDevueltos = gson.fromJson(responseData, new TypeToken<ArrayList<Favoritos>>() {
                    }.getType());
                    listaFavoritos.addAll(favoritosDevueltos);
                    Log.d("OK", "Respuesta exitosa: " + responseData);
                } else {
                    Log.e("KO", "Respuesta no exitosa: " + response.code());
                }
            }
        });
        return listaFavoritos;
    }

    public interface OnPeliculasObtenidasListener {
        void onSuccess(ArrayList<Anime> peliculas);

        void onError(Exception e);
    }

}
