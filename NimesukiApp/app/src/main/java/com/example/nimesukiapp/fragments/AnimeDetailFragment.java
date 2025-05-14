package com.example.nimesukiapp.fragments;

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
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimeDetailFragment extends Fragment {

    private ImageView imageViewAnime;
    private MaterialTextView textNombre, textDescripcion,
            textAnho, textCategorias, textCapitulos;
    private MaterialToolbar toolbar;
    private String imageVersion = "?v=2";
    private boolean isExpanded = false;
    private String fullText;
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
        textAnho = view.findViewById(R.id.textAnho);
        textCategorias = view.findViewById(R.id.textCategorias);
        textCapitulos = view.findViewById(R.id.textCapitulos);
        toolbar = view.findViewById(R.id.topAppBar);

        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle("");

        if (anime != null) {
            textNombre.setText(anime.getNombre());
            textAnho.setText(getString(R.string.release_year) + ": " + anime.getAnhoSalida());
            textCategorias.setText(getString(R.string.categories) + ": " + anime.getCategorias());
            textCapitulos.setText(getString(R.string.episodes) + ": " + anime.getCapTotales());

            fullText = anime.getDescripcion();
            textDescripcion.post(() -> mostrarTextoColapsado()); // importante esperar a que tenga ancho


            Glide.with(this)
                    .load(anime.getImagen() + imageVersion)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .into(imageViewAnime);

            /*textLeerMas.setOnClickListener(v -> {
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
            });*/

        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
    private void mostrarTextoColapsado() {
        textDescripcion.setMaxLines(5);
        textDescripcion.setEllipsize(TextUtils.TruncateAt.END);

        String leerMas = " Leer más";
        String truncated = obtenerTextoTruncado(textDescripcion, fullText, 5, leerMas);

        SpannableString spannable = new SpannableString(truncated + leerMas);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                mostrarTextoExpandido();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getContext(), R.color.pastelAccent));
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.DEFAULT_BOLD);
            }
        };

        spannable.setSpan(clickableSpan, truncated.length(), spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textDescripcion.setText(spannable);
        textDescripcion.setMovementMethod(LinkMovementMethod.getInstance());

        isExpanded = false;
    }

    private void mostrarTextoExpandido() {
        String leerMenos = " Leer menos";
        SpannableString spannable = new SpannableString(fullText + leerMenos);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                mostrarTextoColapsado();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getContext(), R.color.pastelAccent));
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.DEFAULT_BOLD);
            }
        };

        spannable.setSpan(clickableSpan, fullText.length(), spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textDescripcion.setText(spannable);
        textDescripcion.setMovementMethod(LinkMovementMethod.getInstance());

        isExpanded = true;
    }

    private String obtenerTextoTruncado(MaterialTextView textView, String text, int maxLines, String leerMas) {
        TextPaint textPaint = textView.getPaint();
        int width = textView.getWidth();

        if (width <= 0) {
            textView.post(() -> mostrarTextoColapsado());
            return "";
        }

        int end = text.length();
        StaticLayout layout;

        do {
            end--;
            layout = StaticLayout.Builder
                    .obtain(text.substring(0, end) + leerMas, 0, end + leerMas.length(), textPaint, width)
                    .setMaxLines(maxLines)
                    .setEllipsize(TextUtils.TruncateAt.END)
                    .build();
        } while (layout.getLineCount() > maxLines && end > 0);

        return text.substring(0, end).trim();
    }
}