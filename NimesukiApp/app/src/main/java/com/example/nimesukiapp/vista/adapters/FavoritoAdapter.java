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
import com.example.nimesukiapp.models.FavoritosManager;
import com.example.nimesukiapp.models.vo.Favoritos;
import com.example.nimesukiapp.models.vo.FavoritosId;
import com.example.nimesukiapp.models.vo.Usuario;
import com.example.nimesukiapp.vista.fragments.LoginFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FavoritoAdapter extends ArrayAdapter<Favoritos> {
    Context contexto;
    FavoritosManager cacheManager;
    ArrayList<String> favoritosCache;
    private SharedPreferences prefs;
    private String imageVersion = "?v=3";

    public FavoritoAdapter(Context context, ArrayList<Favoritos> favoritos) {
        super(context, 0, favoritos);
        contexto = context;
        cacheManager = new FavoritosManager(contexto);
        prefs = contexto.getSharedPreferences("MisPreferencias", MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_anime, parent, false);
        }

        Favoritos favorito = getItem(position);

        MaterialTextView nombre = convertView.findViewById(R.id.anime_name);
        nombre.setText(favorito.getAnime().getNombre());

        ShapeableImageView imagen = convertView.findViewById(R.id.anime_image);
        Glide.with(contexto)
                .load(favorito.getAnime().getImagen() + imageVersion)
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_profile)
                .into(imagen);

        ImageButton favoriteButton = convertView.findViewById(R.id.favorite_button);
        favoriteButton.setSelected(true);

        favoriteButton.setOnClickListener(v -> {
            favoriteButton.setEnabled(false);
            String usuarioJson = prefs.getString("usuario_completo", null);
            if (usuarioJson != null) {
                Gson gson = new Gson();
                Usuario u = gson.fromJson(usuarioJson, Usuario.class);
                FavoritosId favId = new FavoritosId(u.getIdUsuario(), favorito.getAnime().getIdAnime());
                ServicioREST servicioREST = new ServicioREST(contexto);

                new Thread(() -> {
                    try {
                        servicioREST.eliminarFavorito(favId, new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                ((Activity) contexto).runOnUiThread(() ->
                                        Toast.makeText(contexto, contexto.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                                );
                                favoriteButton.setEnabled(true);
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                ((Activity) contexto).runOnUiThread(() -> {
                                    if (response.isSuccessful()) {
                                        favoritosCache = cacheManager.cargarFavoritos();
                                        favoritosCache.remove(favorito.getAnime().getNombre());
                                        cacheManager.guardarFavoritos(favoritosCache);
                                        remove(favorito);
                                        notifyDataSetChanged();
                                        prefs.edit().putBoolean("cambio", true).apply();
                                        Toast.makeText(contexto, contexto.getString(R.string.delete_favourite_anime_successfully), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(contexto, contexto.getString(R.string.delete_favourite_anime_error), Toast.LENGTH_SHORT).show();
                                    }
                                    favoriteButton.setEnabled(true);
                                });
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((Activity) contexto).runOnUiThread(() -> favoriteButton.setEnabled(true));
                    }
                }).start();
            } else {
                Toast.makeText(contexto, contexto.getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
                favoriteButton.setEnabled(true);
            }
        });

        return convertView;
    }
}