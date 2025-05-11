package com.example.nimesukiapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

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

        TextView nombre = convertView.findViewById(R.id.anime_name);

        nombre.setText(anime.getNombre());

        ImageView imagen = convertView.findViewById(R.id.anime_image);

        Glide.with(contexto)
                .load(anime.getImagen() + imageVersion)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_profile)
                .into(imagen);

        return convertView;
    }
}