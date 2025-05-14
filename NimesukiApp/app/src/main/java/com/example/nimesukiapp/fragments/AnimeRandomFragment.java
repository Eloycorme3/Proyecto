package com.example.nimesukiapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textview.MaterialTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimeRandomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimeRandomFragment extends Fragment {

    private ImageView imageViewAnime;
    private MaterialTextView textNombre, textDescripcion, textLeerMas,
            textAnho, textCategorias, textCapitulos;
    private String imageVersion = "?v=2";

    private boolean isExpanded = false;
    private Anime anime;

    public AnimeRandomFragment(Anime anime) {
        this.anime = anime;
    }

    public AnimeRandomFragment() {}

    public static AnimeRandomFragment newInstance(Anime anime) {
        AnimeRandomFragment fragment = new AnimeRandomFragment(anime);
        Bundle args = new Bundle();
        args.putSerializable("anime", anime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_anime_random, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            anime = (Anime) getArguments().getSerializable("anime");
        }

        imageViewAnime = view.findViewById(R.id.imageViewAnimeRandom);
        textNombre = view.findViewById(R.id.textNombreRandom);
        textDescripcion = view.findViewById(R.id.textDescripcionRandom);
        textLeerMas = view.findViewById(R.id.textLeerMasRandom);
        textAnho = view.findViewById(R.id.textAnhoRandom);
        textCategorias = view.findViewById(R.id.textCategoriasRandom);
        textCapitulos = view.findViewById(R.id.textCapitulosRandom);

        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsingToolbarRandom);
        collapsingToolbar.setTitle("");

        if (anime != null) {
            actualizarVistaAnime(anime);
        }

        textLeerMas.setOnClickListener(v -> {
            if (isExpanded) {
                textDescripcion.setMaxLines(5);
                textDescripcion.setEllipsize(TextUtils.TruncateAt.END);
                textLeerMas.setText(getString(R.string.read_more));
            } else {
                textDescripcion.setMaxLines(Integer.MAX_VALUE);
                textDescripcion.setEllipsize(null);
                textLeerMas.setText(getString(R.string.read_less));
            }
            isExpanded = !isExpanded;
        });
    }

    public void actualizarAnime(Anime nuevoAnime) {
        this.anime = nuevoAnime;
        actualizarVistaAnime(nuevoAnime);
    }

    private void actualizarVistaAnime(Anime anime) {
        if (anime != null) {
            textNombre.setText(anime.getNombre());
            textDescripcion.setText(anime.getDescripcion());
            textAnho.setText(getString(R.string.release_year) + ": " + anime.getAnhoSalida());
            textCategorias.setText(getString(R.string.categories) + ": " + anime.getCategorias());
            textCapitulos.setText(getString(R.string.episodes) + ": " + anime.getCapTotales());

            Glide.with(this)
                    .load(anime.getImagen() + imageVersion)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .into(imageViewAnime);
        }
    }
}
