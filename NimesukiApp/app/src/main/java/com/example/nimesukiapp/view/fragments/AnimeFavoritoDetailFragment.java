package com.example.nimesukiapp.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.model.vo.Favoritos;
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
    private MaterialTextView textNombreFav, textDescripcionFav,
            textAnhoFav, textCategoriasFav, textCapitulosFav;
    private MaterialToolbar toolbar;
    private String imageVersionFav = "?v=3";
    private boolean isExpanded = false;
    private Favoritos animeFav;
    private SharedPreferences prefs;

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
        prefs = getContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        prefs.edit().putBoolean("detalleFavoritos", true).apply();

        if (getArguments() != null) {
            animeFav = (Favoritos) getArguments().getSerializable("animeFavorito");
        }

        imageViewAnimeFav = view.findViewById(R.id.imageViewAnimeFav);
        textNombreFav = view.findViewById(R.id.textNombreFav);
        textDescripcionFav = view.findViewById(R.id.textDescripcionFav);
        textAnhoFav = view.findViewById(R.id.textAnhoFav);
        textCategoriasFav = view.findViewById(R.id.textCategoriasFav);
        textCapitulosFav = view.findViewById(R.id.textCapitulosFav);
        toolbar = view.findViewById(R.id.topAppBarFav);

        CollapsingToolbarLayout collapsingToolbarFav = view.findViewById(R.id.collapsingToolbarFav);
        collapsingToolbarFav.setTitle("");

        if (animeFav != null) {
            textNombreFav.setText(animeFav.getAnime().getNombre());
            textDescripcionFav.setText(animeFav.getAnime().getDescripcion());
            textAnhoFav.setText(getString(R.string.release_year) + ": " + animeFav.getAnime().getAnhoSalida());
            textCategoriasFav.setText(getString(R.string.categories) + ": " + animeFav.getAnime().getCategorias());
            textCapitulosFav.setText(getString(R.string.episodes) + ": " + animeFav.getAnime().getCapTotales());

            setupExpandableText(textDescripcionFav, animeFav.getAnime().getDescripcion(), 3);

            Glide.with(requireContext())
                    .load(animeFav.getAnime().getImagen() + imageVersionFav)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .into(imageViewAnimeFav);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
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