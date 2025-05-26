package com.example.nimesukiapp.vista.fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.model.vo.Anime;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textview.MaterialTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimeRandomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimeRandomFragment extends Fragment {
    private ImageView imageViewAnimeRandom;
    private MaterialTextView textNombreRandom, textDescripcionRandom,
            textAnhoRandom, textCategoriasRandom, textCapitulosRandom;
    private String imageVersionRandom = "?v=3";
    private boolean isExpanded = false;
    private Anime animeRandom;

    public AnimeRandomFragment(Anime anime) {
        this.animeRandom = anime;
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
            animeRandom = (Anime) getArguments().getSerializable("anime");
        }
        imageViewAnimeRandom = view.findViewById(R.id.imageViewAnimeRandom);
        textNombreRandom = view.findViewById(R.id.textNombreRandom);
        textDescripcionRandom = view.findViewById(R.id.textDescripcionRandom);
        textAnhoRandom = view.findViewById(R.id.textAnhoRandom);
        textCategoriasRandom = view.findViewById(R.id.textCategoriasRandom);
        textCapitulosRandom = view.findViewById(R.id.textCapitulosRandom);

        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsingToolbarRandom);
        collapsingToolbar.setTitle("");

        if (animeRandom != null) {
            try {
                actualizarVistaAnime(animeRandom);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void actualizarAnime(Anime nuevoAnime) throws InterruptedException {
        this.animeRandom = nuevoAnime;
        if (isAdded()) {
            actualizarVistaAnime(animeRandom);
        }
    }

    private void actualizarVistaAnime(Anime anime) throws InterruptedException {
        if (anime != null) {
            textNombreRandom.setText(anime.getNombre());
            textDescripcionRandom.setText(anime.getDescripcion());
            textAnhoRandom.setText(getString(R.string.release_year) + ": " + anime.getAnhoSalida());
            textCategoriasRandom.setText(getString(R.string.categories) + ": " + anime.getCategorias());
            textCapitulosRandom.setText(getString(R.string.episodes) + ": " + anime.getCapTotales());

            setupExpandableText(textDescripcionRandom, anime.getDescripcion(), 3);

            Glide.with(this)
                    .load(anime.getImagen() + imageVersionRandom)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .into(imageViewAnimeRandom);
        }
    }

    private void setupExpandableText(MaterialTextView textView, String descripcion, int maxLines) {
        String mostrar = getString(R.string.read_more);
        String ocultar = getString(R.string.read_less);
        String puntos = "...";

        textView.setMaxLines(Integer.MAX_VALUE);

        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                TextPaint paint = textView.getPaint();
                int lineHeight = textView.getLineHeight();
                int maxHeight = lineHeight * maxLines;

                Runnable updateText = new Runnable() {
                    @Override
                    public void run() {
                        if (isExpanded) {
                            String textoCompleto = descripcion + " " + ocultar;
                            SpannableString spannable = new SpannableString(textoCompleto);

                            int start = textoCompleto.lastIndexOf(ocultar);
                            int end = textoCompleto.length();

                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View widget) {
                                    isExpanded = false;
                                    run();
                                }

                                @Override
                                public void updateDrawState(@NonNull TextPaint tp) {
                                    super.updateDrawState(tp);
                                    tp.setColor(ContextCompat.getColor(textView.getContext(), R.color.pastelPrimary));
                                    tp.setUnderlineText(true);
                                    tp.setFakeBoldText(true);
                                }
                            };

                            spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            textView.setText(spannable);
                            textView.setMovementMethod(LinkMovementMethod.getInstance());

                        } else {
                            String textoExtra = puntos + " " + mostrar;
                            String textoFinal = "";

                            for (int i = 1; i <= descripcion.length(); i++) {
                                String limite = descripcion.substring(0, i);
                                StaticLayout layout = StaticLayout.Builder.obtain(
                                        limite + textoExtra, 0, (limite + textoExtra).length(), paint, textView.getWidth()
                                ).build();

                                if (layout.getHeight() > maxHeight) break;
                                textoFinal = limite;
                            }

                            if (textoFinal.isEmpty()) textoFinal = descripcion.length() > 5 ? descripcion.substring(0, 5) : descripcion;

                            String textoCompleto = textoFinal + textoExtra;
                            SpannableString spannable = new SpannableString(textoCompleto);

                            int start = textoCompleto.lastIndexOf(mostrar);
                            int end = textoCompleto.length();

                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View widget) {
                                    isExpanded = true;
                                    run();
                                }

                                @Override
                                public void updateDrawState(@NonNull TextPaint tp) {
                                    super.updateDrawState(tp);
                                    tp.setColor(ContextCompat.getColor(textView.getContext(), R.color.pastelPrimary));
                                    tp.setUnderlineText(true);
                                    tp.setFakeBoldText(true);
                                }
                            };

                            spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            textView.setText(spannable);
                            textView.setMovementMethod(LinkMovementMethod.getInstance());
                        }
                    }
                };

                updateText.run();
            }
        });
    }

}
