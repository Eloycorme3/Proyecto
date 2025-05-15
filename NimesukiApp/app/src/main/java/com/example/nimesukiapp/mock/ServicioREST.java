package com.example.nimesukiapp.mock;

import android.util.Log;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.nimesukiapp.models.vo.Anime;
import com.example.nimesukiapp.models.vo.Favoritos;
import com.example.nimesukiapp.models.vo.Usuario;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServicioREST {
    String localhost = "10.0.2.2";

    public void registrarUsuario(String nombreUsuario, String contrasenha, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        String json = "{"
                + "\"nombreUsuario\":\"" + nombreUsuario + "\","
                + "\"contrasenha\":\"" + contrasenha + "\""
                + "}";
        //Cambiar y pasarle objeto sin id (String json = gson.toJson(item);)

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("http://" + localhost + "8088/usuarios")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public Anime obtenerAnimeNoFavorito(String nombreUsuario) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + localhost + ":8088/animes/no-favoritos/" + nombreUsuario)
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

    public void obtenerUsuarioPorNombre(String username, OnUsuarioObtenidoListener listener) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + localhost + ":8088/usuarios/" + username)
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
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Usuario>>() {}.getType();
                    List<Usuario> usuarios = gson.fromJson(responseData, listType);

                    listener.onSuccess(usuarios.get(0));
                } else {
                    Log.e("KO", "Respuesta no exitosa: " + response.code());
                    listener.onError(new IOException("Respuesta no exitosa: " + response.code()));
                }
            }
        });
    }

    public ArrayList<Anime> obtenerAnimes(OnAnimesObtenidosListener listener) {
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

    public ArrayList<Anime> obtenerAnimesPorNombre(String nombre, OnAnimesObtenidosPorNombreListener listener) throws IOException {
        ArrayList<Anime> animesPorNombre = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + localhost + ":8088/animes/" + nombre)
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
                    animesPorNombre.clear();
                    String responseData = response.body().string();
                    ArrayList<Anime> animesPorNombreDevueltos;
                    Gson gson = new Gson();
                    animesPorNombreDevueltos = gson.fromJson(responseData, new TypeToken<ArrayList<Favoritos>>() {
                    }.getType());
                    animesPorNombre.addAll(animesPorNombreDevueltos);
                    Log.d("OK", "Respuesta exitosa: " + responseData);
                    listener.onSuccess(animesPorNombreDevueltos);
                } else {
                    Log.e("KO", "Respuesta no exitosa: " + response.code());
                    listener.onError(new IOException("Respuesta no exitosa: " + response.code()));
                }
            }
        });
        return animesPorNombre;
    }

    public ArrayList<Favoritos> obtenerFavoritosPorUsuario(String nombreUsuario, OnAnimesFavoritosObtenidosListener listener) {
        ArrayList<Favoritos> listaFavoritos = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + localhost + ":8088/favoritos/" + nombreUsuario)
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
                    listaFavoritos.clear();
                    String responseData = response.body().string();
                    ArrayList<Favoritos> favoritosDevueltos;
                    Gson gson = new Gson();
                    favoritosDevueltos = gson.fromJson(responseData, new TypeToken<ArrayList<Favoritos>>() {
                    }.getType());
                    listaFavoritos.addAll(favoritosDevueltos);
                    Log.d("OK", "Respuesta exitosa: " + responseData);
                    listener.onSuccess(favoritosDevueltos);
                } else {
                    Log.e("KO", "Respuesta no exitosa: " + response.code());
                    listener.onError(new IOException("Respuesta no exitosa: " + response.code()));
                }
            }
        });
        return listaFavoritos;
    }

    public interface OnUsuarioObtenidoListener {
        void onSuccess(Usuario usuario);

        void onError(Exception e);
    }


    public interface OnAnimesObtenidosListener {
        void onSuccess(ArrayList<Anime> animes);

        void onError(Exception e);
    }

    public interface OnAnimesFavoritosObtenidosListener {
        void onSuccess(ArrayList<Favoritos> favoritos);

        void onError(Exception e);
    }

    public interface OnAnimesObtenidosPorNombreListener {
        void onSuccess(ArrayList<Anime> animes);

        void onError(Exception e);
    }


}
