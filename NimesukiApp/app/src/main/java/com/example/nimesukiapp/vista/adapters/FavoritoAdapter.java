package com.example.nimesukiapp.vista.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Favoritos;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class FavoritoAdapter extends ArrayAdapter<Favoritos> {
    Context contexto;
    private String imageVersion = "?v=2";

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
                .load(favorito.getAnime().getImagen() + imageVersion)
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_profile)
                .into(imagen);

        return convertView;
    }
}
