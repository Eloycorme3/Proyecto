package com.example.nimesukiapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Anime;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    private List<Anime> animeList;
    private Context context;

    public AnimeAdapter(List<Anime> animeList, Context context) {
        this.animeList = animeList;
        this.context = context;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_anime, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        Anime anime = animeList.get(position);
        holder.nameTextView.setText(anime.getNombre());
        holder.descriptionTextView.setText(anime.getDescripcion());

        // Si la descripción es demasiado larga, establecer "Leer más"
        if (anime.getDescripcion().length() > 100) {
            String shortDescription = anime.getDescripcion().substring(0, 100) + "... Leer más";
            holder.descriptionTextView.setText(shortDescription);
        }

        holder.descriptionTextView.setOnClickListener(v -> {
            // Alternar entre "Leer más" y mostrar la descripción completa
            String currentText = holder.descriptionTextView.getText().toString();
            if (currentText.endsWith("Leer más")) {
                holder.descriptionTextView.setText(anime.getDescripcion());  // Mostrar todo el texto
            } else {
                String shortDescription = anime.getDescripcion().substring(0, 100) + "... Leer más";
                holder.descriptionTextView.setText(shortDescription);  // Truncar el texto
            }
        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView descriptionTextView;

        public AnimeViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.anime_name);
            descriptionTextView = itemView.findViewById(R.id.anime_description);
        }
    }
}

