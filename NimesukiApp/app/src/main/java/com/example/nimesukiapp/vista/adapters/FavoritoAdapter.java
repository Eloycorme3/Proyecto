package com.example.nimesukiapp.vista.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Favoritos;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class FavoritoAdapter extends ArrayAdapter<Favoritos> {
    Context contexto;

    public FavoritoAdapter(Context context, ArrayList<Favoritos> favoritos) {
        super(context, 0, favoritos);
        contexto = context;
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
                .load(favorito.getAnime().getImagen())
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_profile)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideError", "Fallo al cargar: " + favorito.getAnime().getNombre() + " → " + favorito.getAnime().getImagen(), e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imagen);

        return convertView;
    }
}
