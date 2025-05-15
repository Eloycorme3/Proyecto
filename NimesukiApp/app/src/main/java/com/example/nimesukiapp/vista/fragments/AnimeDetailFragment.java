package com.example.nimesukiapp.vista.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

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

            String  descripcion = anime.getDescripcion();
            makeTextViewExpandable(textDescripcion, descripcion, 3);


            Glide.with(this)
                    .load(anime.getImagen() + imageVersion)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .into(imageViewAnime);

        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    public void makeTextViewExpandable(MaterialTextView textView, String descripcion, int maxLines) {
        textView.setMaxLines(maxLines);
        textView.setEllipsize(TextUtils.TruncateAt.END);

        ViewTreeObserver vto = textView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (textView.getLineCount() <= maxLines) {
                    textView.setText(descripcion);
                    return;
                }

                int lineEndIndex = textView.getLayout().getLineEnd(maxLines - 1);
                String truncatedText = descripcion.substring(0, lineEndIndex).trim();

                SpannableString spannableCollapsed = new SpannableString(truncatedText + "..." + getString(R.string.read_more));
                ClickableSpan expandSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        SpannableString spannableExpanded = new SpannableString(descripcion + getString(R.string.read_less));
                        ClickableSpan collapseSpan = new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                textView.setText(spannableCollapsed);
                                textView.setMovementMethod(LinkMovementMethod.getInstance());
                            }
                        };
                        spannableExpanded.setSpan(collapseSpan, descripcion.length(),
                                descripcion.length() + getString(R.string.read_less).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        textView.setText(spannableExpanded);
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                };
                spannableCollapsed.setSpan(expandSpan, truncatedText.length() + 3,
                        truncatedText.length() + 3 + getString(R.string.read_more).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                textView.setText(spannableCollapsed);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }
}