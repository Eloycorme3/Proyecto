package com.example.nimesukiapp.mock;

import android.content.SharedPreferences;
import android.util.Log;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.nimesukiapp.models.vo.Anime;
import com.example.nimesukiapp.models.vo.Favoritos;
import com.example.nimesukiapp.models.vo.FavoritosId;
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

    private SharedPreferences prefs;
    private String localhost = "127.0.0.1";

    private String nombreBD;
    private String contrasenhaBD;
    private String ip;

    public ServicioREST(Context context) {
        prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        nombreBD = prefs.getString("nombreBD", "");
        contrasenhaBD = prefs.getString("contrasenhaBD", "");
        ip = prefs.getString("ip", "");
    }

    private String baseUrl() {
        return "http://" + localhost + ":8088";
    }

    private String authParams() {
        return "?ip=" + ip + "&user=" + nombreBD + "&pass=" + contrasenhaBD;
    }

    public void registrarUsuario(Usuario u, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(u);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(baseUrl() + "/usuarios" + authParams())
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public Anime obtenerAnimeNoFavorito(String nombreUsuario) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl() + "/animes/no-favoritos/" + nombreUsuario + authParams())
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

    public void obtenerUsuarioPorNombre(String nombreUsuario, OnUsuarioObtenidoListener listener) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl() + "/usuarios/" + nombreUsuario + authParams())
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
                    Type listType = new TypeToken<List<Usuario>>() {
                    }.getType();
                    List<Usuario> usuarios = gson.fromJson(responseData, listType);

                    if (usuarios != null && !usuarios.isEmpty()) {
                        listener.onSuccess(usuarios.get(0));
                    } else {
                        listener.onError(new IOException("Respuesta no exitosa: " + response.code()));
                    }
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
                .url(baseUrl() + "/animes" + authParams())
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

    public ArrayList<Anime> obtenerAnimesPorNombre(String nombre, OnAnimesObtenidosPorNombreListener listener) {
        ArrayList<Anime> animesPorNombre = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl() + "/animes/" + nombre + authParams())
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
                    animesPorNombreDevueltos = gson.fromJson(responseData, new TypeToken<ArrayList<Anime>>() {
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

    public ArrayList<Favoritos> obtenerFavoritosPorNombre(String nombreUsuario, String nombreAnime, OnAnimesFavoritosObtenidosPorNombreListener listener) {
        ArrayList<Favoritos> favoritosPorNombre = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl() + "/favoritos/" + nombreUsuario + "/" + nombreAnime + authParams())
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
                    favoritosPorNombre.clear();
                    String responseData = response.body().string();
                    ArrayList<Favoritos> favoritosPorNombreDevueltos;
                    Gson gson = new Gson();
                    favoritosPorNombreDevueltos = gson.fromJson(responseData, new TypeToken<ArrayList<Favoritos>>() {
                    }.getType());
                    favoritosPorNombre.addAll(favoritosPorNombreDevueltos);
                    Log.d("OK", "Respuesta exitosa: " + responseData);
                    listener.onSuccess(favoritosPorNombreDevueltos);
                } else {
                    Log.e("KO", "Respuesta no exitosa: " + response.code());
                    listener.onError(new IOException("Respuesta no exitosa: " + response.code()));
                }
            }
        });
        return favoritosPorNombre;
    }

    public ArrayList<Favoritos> obtenerFavoritosPorUsuario(String nombreUsuario, OnAnimesFavoritosObtenidosListener listener) {
        ArrayList<Favoritos> listaFavoritos = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl() + "/favoritos/" + nombreUsuario + authParams())
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

    public void crearFavorito(Favoritos favorito, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(favorito);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(baseUrl() + "/favoritos" + authParams())
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void modificarFavorito(Favoritos favorito, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(favorito);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(baseUrl() + "/favoritos" + authParams())
                .put(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void eliminarFavorito(FavoritosId id, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(id);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(baseUrl() + "/favoritos" + authParams())
                .delete(body)
                .build();

        client.newCall(request).enqueue(callback);
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

    public interface OnAnimesFavoritosObtenidosPorNombreListener {
        void onSuccess(ArrayList<Favoritos> favoritos);

        void onError(Exception e);
    }

}
