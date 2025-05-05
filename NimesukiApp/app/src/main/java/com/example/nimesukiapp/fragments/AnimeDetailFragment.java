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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimeDetailFragment extends Fragment {

    private ImageView imageViewAnime;
    private TextView textNombre, textDescripcion, textLeerMas,
            textAnho, textCategorias, textCapitulos;

    private boolean isExpanded = false;
    private Anime anime;

    public AnimeDetailFragment() {
    }

    public static AnimeDetailFragment newInstance(Anime anime) {
        AnimeDetailFragment fragment = new AnimeDetailFragment();
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
        return inflater.inflate(R.layout.fragment_anime_detail, container, false);
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

        imageViewAnime = view.findViewById(R.id.imageViewAnime);
        textNombre = view.findViewById(R.id.textNombre);
        textDescripcion = view.findViewById(R.id.textDescripcion);
        textLeerMas = view.findViewById(R.id.textLeerMas);
        textAnho = view.findViewById(R.id.textAnho);
        textCategorias = view.findViewById(R.id.textCategorias);
        textCapitulos = view.findViewById(R.id.textCapitulos);

        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle("");

        if (anime != null) {
            textNombre.setText(anime.getNombre());
            textDescripcion.setText(anime.getDescripcion());
            textAnho.setText("Año de salida: " + anime.getAnhoSalida());
            textCategorias.setText("Categorías: " + anime.getCategorias());
            textCapitulos.setText("Capítulos totales: " + anime.getCapTotales());

            Glide.with(this)
                    .load(anime.getImagen())
                    .into(imageViewAnime);

            textLeerMas.setOnClickListener(v -> {
                if (isExpanded) {
                    textDescripcion.setMaxLines(5);
                    textDescripcion.setEllipsize(TextUtils.TruncateAt.END);
                    textLeerMas.setText("Leer más");
                } else {
                    textDescripcion.setMaxLines(Integer.MAX_VALUE);
                    textDescripcion.setEllipsize(null);
                    textLeerMas.setText("Leer menos");
                }
                isExpanded = !isExpanded;
            });
        }
    }
}