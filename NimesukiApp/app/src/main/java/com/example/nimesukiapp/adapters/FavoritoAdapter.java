package com.example.nimesukiapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Favoritos;

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

        TextView nombre = convertView.findViewById(R.id.anime_name);

        nombre.setText(favorito.getAnime().getNombre());

        ImageView imagen = convertView.findViewById(R.id.anime_image);

        Glide.with(contexto)
                .load(favorito.getAnime().getImagen())
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_profile)
                .into(imagen);

        return convertView;
    }
}
