package com.example.nimesukiapp.vista.fragments;

import android.os.Build;
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
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Favoritos;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimeFavoritoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimeFavoritoDetailFragment extends Fragment {

    private ImageView imageViewAnimeFav;
    private MaterialTextView textNombreFav, textDescripcionFav, textLeerMasFav,
            textAnhoFav, textCategoriasFav, textCapitulosFav;
    private MaterialToolbar toolbar;
    private String imageVersionFav = "?v=2";
    private boolean isExpanded = false;
    private Favoritos animeFav;

    public AnimeFavoritoDetailFragment() {
    }

    public static AnimeFavoritoDetailFragment newInstance(Favoritos animeFavorito) {
        AnimeFavoritoDetailFragment fragment = new AnimeFavoritoDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("animeFavorito", animeFavorito);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_anime_favorito_detail, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            animeFav = (Favoritos) getArguments().getSerializable("animeFavorito");
        }

        imageViewAnimeFav = view.findViewById(R.id.imageViewAnimeFav);
        textNombreFav = view.findViewById(R.id.textNombreFav);
        textDescripcionFav = view.findViewById(R.id.textDescripcionFav);
        textLeerMasFav = view.findViewById(R.id.textLeerMasFav);
        textAnhoFav = view.findViewById(R.id.textAnhoFav);
        textCategoriasFav = view.findViewById(R.id.textCategoriasFav);
        textCapitulosFav = view.findViewById(R.id.textCapitulosFav);
        toolbar = view.findViewById(R.id.topAppBar);

        CollapsingToolbarLayout collapsingToolbarFav = view.findViewById(R.id.collapsingToolbarFav);
        collapsingToolbarFav.setTitle("");

        if (animeFav != null) {
            textNombreFav.setText(animeFav.getAnime().getNombre());
            textDescripcionFav.setText(animeFav.getAnime().getDescripcion());
            textAnhoFav.setText(getString(R.string.release_year) + ": " + animeFav.getAnime().getAnhoSalida());
            textCategoriasFav.setText(getString(R.string.categories) + ": " + animeFav.getAnime().getCategorias());
            textCapitulosFav.setText(getString(R.string.episodes) + ": " + animeFav.getAnime().getCapTotales());

            Glide.with(requireContext())
                    .load(animeFav.getAnime().getImagen() + imageVersionFav)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .into(imageViewAnimeFav);

            textLeerMasFav.setOnClickListener(v -> {
                if (isExpanded) {
                    textDescripcionFav.setMaxLines(5);
                    textDescripcionFav.setEllipsize(TextUtils.TruncateAt.END);
                    textLeerMasFav.setText(getString(R.string.read_more));
                } else {
                    textDescripcionFav.setMaxLines(Integer.MAX_VALUE);
                    textDescripcionFav.setEllipsize(null);
                    textLeerMasFav.setText(getString(R.string.read_less));
                }
                isExpanded = !isExpanded;
            });
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

    }
}