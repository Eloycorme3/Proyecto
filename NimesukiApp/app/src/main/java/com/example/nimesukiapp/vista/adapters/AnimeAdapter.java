package com.example.nimesukiapp.vista.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class AnimeAdapter extends ArrayAdapter<Anime> {
    Context contexto;
    private String imageVersion = "?v=2";

    public AnimeAdapter(Context context, ArrayList<Anime> animes) {
        super(context, 0, animes);
        contexto = context;
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


        favoriteButton.setOnClickListener(v -> {
            boolean currentlySelected = favoriteButton.isSelected();
            boolean newState = !currentlySelected;

            favoriteButton.setEnabled(false);

            new Thread(() -> {
                boolean success = false;

                try {
                    if (newState) {
                        // Insertar en favoritos
                    } else {
                        // Eliminar de favoritos
                    }
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
        });



        return convertView;
    }
}