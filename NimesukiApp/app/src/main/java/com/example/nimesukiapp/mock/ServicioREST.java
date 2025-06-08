package com.example.nimesukiapp.mock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.model.vo.Anime;
import com.example.nimesukiapp.model.vo.Favoritos;
import com.example.nimesukiapp.model.vo.FavoritosId;
import com.example.nimesukiapp.model.vo.Usuario;
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
    private String localhost = "10.0.2.2";
    private String nombreBD;
    private String contrasenhaBD;
    private String ip;
    private Context contexto;

    public ServicioREST(Context context) {
        contexto = context;
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

    private boolean comprobar() {
        if (prefs.getString("nombreBD", "") != "" && prefs.getString("contrasenhaBD", "") != "" && prefs.getString("ip", "") != "") {
            return true;
        }
        return false;
    }

    public void registrarUsuario(Usuario u, Callback callback) {
        if (comprobar()) {
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
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
        }
    }

    public void obtenerUsuarioPorNombre(String nombreUsuario, OnUsuarioObtenidoListener listener) {
        if (comprobar()) {
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
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
        }
    }

    public void actualizarUsuario(Usuario u, Callback callback) {
        if (comprobar()) {
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();
            String json = gson.toJson(u);

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(baseUrl() + "/usuarios/" + u.getIdUsuario() + authParams())
                    .put(body)
                    .build();

            client.newCall(request).enqueue(callback);
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
        }
    }

    public void eliminarUsuario(Integer idUsuario, Callback callback) {
        if (comprobar()) {
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();
            String json = gson.toJson(idUsuario);

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(baseUrl() + "/usuarios" + authParams())
                    .delete(body)
                    .build();

            client.newCall(request).enqueue(callback);
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
        }
    }

    public Anime obtenerAnimePorId(Integer id, OnAnimeObtenidoPorIdListener listener) {
        if (comprobar()) {
            final Anime[] a = {null};
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(baseUrl() + "/animes/search/" + id + authParams())
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
                        Anime animeDevuelto;
                        Gson gson = new Gson();
                        animeDevuelto = gson.fromJson(responseData, new TypeToken<Anime>() {
                        }.getType());
                        a[0] = animeDevuelto;
                        Log.d("OK", "Respuesta exitosa: " + responseData);
                        listener.onSuccess(animeDevuelto);
                    } else {
                        Log.e("KO", "Respuesta no exitosa: " + response.code());
                        listener.onError(new IOException("Respuesta no exitosa: " + response.code()));
                    }
                }
            });
            return a[0];
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
            return null;
        }
    }

    public Anime obtenerAnimeNoFavorito(String nombreUsuario) throws IOException {
        if (comprobar()) {
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
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
            return null;
        }
    }

    public ArrayList<Anime> obtenerAnimes(OnAnimesObtenidosListener listener) {
        if (comprobar()) {
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
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
            return null;
        }
    }

    public ArrayList<Anime> obtenerAnimesPorNombre(String nombre, OnAnimesObtenidosPorNombreListener listener) {
        if (comprobar()) {
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
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
            return null;
        }
    }

    public Favoritos obtenerFavoritosPorId(String nombreUsuario, Integer idAnime, OnAnimesFavoritosObtenidosPorIdListener listener) {
        if (comprobar()) {
            final Favoritos[] favoritoPorId = {null};
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(baseUrl() + "/favoritos/search/" + nombreUsuario + "/" + idAnime + authParams())
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
                        Favoritos favoritoPorIdDevuelto = null;
                        Gson gson = new Gson();
                        favoritoPorIdDevuelto = gson.fromJson(responseData, new TypeToken<Favoritos>() {
                        }.getType());
                        favoritoPorId[0] = favoritoPorIdDevuelto;
                        Log.d("OK", "Respuesta exitosa: " + responseData);
                        listener.onSuccess(favoritoPorIdDevuelto);
                    } else {
                        Log.e("KO", "Respuesta no exitosa: " + response.code());
                        listener.onError(new IOException("Respuesta no exitosa: " + response.code()));
                    }
                }
            });
            return favoritoPorId[0];
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
            return null;
        }
    }

    public ArrayList<Favoritos> obtenerFavoritosPorNombre(String nombreUsuario, String nombreAnime, OnAnimesFavoritosObtenidosPorNombreListener listener) {
        if (comprobar()) {
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
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
            return null;
        }
    }

    public ArrayList<Favoritos> obtenerFavoritosPorUsuario(String nombreUsuario, OnAnimesFavoritosObtenidosListener listener) {
        if (comprobar()) {
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
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
            return null;
        }
    }

    public void crearFavorito(Favoritos favorito, Callback callback) {
        if (comprobar()) {
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
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
        }
    }

    public void actualizarFavorito(Favoritos favorito, Callback callback) {
        if (comprobar()) {
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
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
        }
    }

    public void eliminarFavorito(FavoritosId id, Callback callback) {
        if (comprobar()) {
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
        } else {
            ((Activity) contexto).runOnUiThread(() ->
                    Toast.makeText(contexto, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            );
        }
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

    public interface OnAnimeObtenidoPorIdListener {
        void onSuccess(Anime a);

        void onError(Exception e);
    }

    public interface OnAnimesFavoritosObtenidosPorIdListener {
        void onSuccess(Favoritos f);

        void onError(Exception e);
    }


}
