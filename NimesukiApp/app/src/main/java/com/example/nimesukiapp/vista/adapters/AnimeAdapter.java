package com.example.nimesukiapp.vista.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.models.FavoritosCacheManager;
import com.example.nimesukiapp.models.vo.Anime;
import com.example.nimesukiapp.models.vo.Favoritos;
import com.example.nimesukiapp.models.vo.FavoritosId;
import com.example.nimesukiapp.models.vo.Usuario;
import com.example.nimesukiapp.vista.fragments.CatalogFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AnimeAdapter extends ArrayAdapter<Anime> {
    Context contexto;
    FavoritosCacheManager cacheManager;
    ArrayList<String> favoritosCache;
    private SharedPreferences prefs;
    private String imageVersion = "?v=2";

    public AnimeAdapter(Context context, ArrayList<Anime> animes) {
        super(context, 0, animes);
        contexto = context;
        cacheManager = new FavoritosCacheManager(contexto);
        prefs = contexto.getSharedPreferences("MisPreferencias", MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_anime, parent, false);
        }

        Anime anime = getItem(position);

        MaterialTextView nombre = convertView.findViewById(R.id.anime_name);
        nombre.setText(anime.getNombre());

        ShapeableImageView imagen = convertView.findViewById(R.id.anime_image);
        Glide.with(contexto)
                .load(anime.getImagen() + imageVersion)
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_profile)
                .into(imagen);

        ImageButton favoriteButton = convertView.findViewById(R.id.favorite_button);

        favoritosCache = cacheManager.cargarFavoritos();

        boolean esFavorito = favoritosCache.contains(anime.getNombre());
        favoriteButton.setSelected(esFavorito);

        favoriteButton.setOnClickListener(v -> {
            boolean currentlySelected = favoriteButton.isSelected();
            boolean newState = !currentlySelected;

            favoriteButton.setEnabled(false);
            String usuarioJson = prefs.getString("usuario_completo", null);
            if (usuarioJson != null) {
                Gson gson = new Gson();
                Usuario u = gson.fromJson(usuarioJson, Usuario.class);
                FavoritosId favId = new FavoritosId(u.getIdUsuario(), anime.getIdAnime());
                ServicioREST servicioREST = new ServicioREST(contexto);
                new Thread(() -> {
                    boolean success = false;
                    try {
                        if (newState) {
                            Favoritos f = new Favoritos(favId, anime, u, 0, 0);
                            servicioREST.crearFavorito(f, new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    ((Activity) contexto).runOnUiThread(() ->
                                            Toast.makeText(contexto, contexto.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                                    );
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) {
                                    if (response.isSuccessful()) {
                                        ((Activity) contexto).runOnUiThread(() ->
                                                Toast.makeText(contexto, contexto.getString(R.string.create_favourite_anime_successfully), Toast.LENGTH_SHORT).show()
                                        );
                                    } else {
                                        ((Activity) contexto).runOnUiThread(() ->
                                                Toast.makeText(contexto, contexto.getString(R.string.create_favourite_anime_error), Toast.LENGTH_SHORT).show()
                                        );
                                    }
                                }
                            });
                            favoritosCache.add(anime.getNombre());
                        } else {
                            servicioREST.eliminarFavorito(favId, new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    ((Activity) contexto).runOnUiThread(() ->
                                            Toast.makeText(contexto, contexto.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                                    );
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) {
                                    if (response.isSuccessful()) {
                                        ((Activity) contexto).runOnUiThread(() ->
                                                Toast.makeText(contexto, contexto.getString(R.string.delete_favourite_anime_successfully), Toast.LENGTH_SHORT).show()
                                        );
                                    } else {
                                        ((Activity) contexto).runOnUiThread(() ->
                                                Toast.makeText(contexto, contexto.getString(R.string.delete_favourite_anime_error), Toast.LENGTH_SHORT).show()
                                        );
                                    }
                                }
                            });
                            favoritosCache.remove(anime.getNombre());
                        }

                        cacheManager.guardarFavoritos(favoritosCache);

                        success = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    boolean finalSuccess = success;
                    ((Activity) contexto).runOnUiThread(() -> {
                        if (finalSuccess) {
                            favoriteButton.setSelected(newState);
                        }
                        favoriteButton.setEnabled(true);
                    });
                }).start();
            } else {
                ((Activity) contexto).runOnUiThread(() -> {
                    Toast.makeText(contexto, contexto.getString(R.string.create_favourite_anime_error), Toast.LENGTH_SHORT).show();
                });
            }
        });

        return convertView;
    }
}