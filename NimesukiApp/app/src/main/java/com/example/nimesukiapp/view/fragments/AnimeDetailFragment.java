package com.example.nimesukiapp.view.fragments;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.FavoritosManager;
import com.example.nimesukiapp.model.vo.Anime;
import com.example.nimesukiapp.model.vo.Favoritos;
import com.example.nimesukiapp.model.vo.FavoritosId;
import com.example.nimesukiapp.model.vo.Usuario;
import com.example.nimesukiapp.view.activities.ListaAnimesFavoritosView;
import com.example.nimesukiapp.view.activities.ListaAnimesView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimeDetailFragment extends Fragment {
    private ImageView imageViewAnime;
    private MaterialTextView textNombre, textDescripcion,
            textAnho, textCategorias, textCapitulos;
    private FloatingActionButton btnFloatingFav;
    private ProgressBar loading;
    private MaterialToolbar toolbar;
    private String imageVersion = "?v=3";
    private boolean isExpanded = false;
    private Anime anime;
    private FavoritosManager cacheManager;
    private ArrayList<String> favoritosCache;
    private SharedPreferences prefs;

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
        prefs = requireContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        prefs.edit().putBoolean("detalle", true).apply();

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
        btnFloatingFav = view.findViewById(R.id.btnFloatingFav);
        loading = view.findViewById(R.id.progressBarLoadingAnimeDetail);

        mostrarProgress(false);

        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle("");

        if (anime != null) {
            textNombre.setText(anime.getNombre());
            textAnho.setText(getString(R.string.release_year) + ": " + anime.getAnhoSalida());
            textCategorias.setText(getString(R.string.categories) + ": " + anime.getCategorias());
            textCapitulos.setText(getString(R.string.episodes) + ": " + anime.getCapTotales());

            setupExpandableText(textDescripcion, anime.getDescripcion(), 3);

            Glide.with(requireContext())
                    .load(anime.getImagen() + imageVersion)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .into(imageViewAnime);
        }

        cacheManager = new FavoritosManager(requireContext());

        favoritosCache = cacheManager.cargarFavoritos();

        btnFloatingFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFloatingFav.setEnabled(false);
                String usuarioJson = prefs.getString("usuario_completo", null);
                if (usuarioJson != null) {
                    Gson gson = new Gson();
                    Usuario u = gson.fromJson(usuarioJson, Usuario.class);
                    FavoritosId favId = new FavoritosId(u.getIdUsuario(), anime.getIdAnime());
                    ServicioREST servicioREST = new ServicioREST(requireContext());
                    new Thread(() -> {
                        try {
                            Favoritos f = new Favoritos(favId, anime, u, 0f, 0);
                            servicioREST.crearFavorito(f, new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    (requireActivity()).runOnUiThread(() ->
                                            Toast.makeText(requireContext(), requireContext().getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                                    );
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) {
                                    if (response.isSuccessful()) {
                                        (requireActivity()).runOnUiThread(() -> {
                                            Toast.makeText(requireContext(), requireContext().getString(R.string.create_favourite_anime_successfully), Toast.LENGTH_SHORT).show();
                                            favoritosCache.add(anime.getNombre());
                                            cacheManager.guardarFavoritos(favoritosCache);
                                            AnimeFavoritoDetailFragment animeFavoritoDetailFragment = AnimeFavoritoDetailFragment.newInstance(f);
                                            //mostrarProgress(true);
                                            if (requireActivity() instanceof ListaAnimesView) {
                                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                                    requireActivity().getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .setCustomAnimations(0, 0)
                                                            .replace(R.id.fragmentContainerLista, animeFavoritoDetailFragment)
                                                            .commit();

                                                    //mostrarProgress(false);
                                                }, 100);
                                            } else if (requireActivity() instanceof ListaAnimesFavoritosView) {
                                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                                    requireActivity().getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .setCustomAnimations(0, 0)
                                                            .replace(R.id.fragmentContainerFavoritos, animeFavoritoDetailFragment)
                                                            .commit();

                                                    mostrarProgress(false);
                                                }, 500);
                                            } else {
                                                mostrarProgress(false);
                                            }
                                        });
                                    } else {
                                        (requireActivity()).runOnUiThread(() ->
                                                Toast.makeText(requireContext(), requireContext().getString(R.string.create_favourite_anime_error), Toast.LENGTH_SHORT).show()
                                        );
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        (requireActivity()).runOnUiThread(() -> btnFloatingFav.setEnabled(true));
                    }).start();
                } else {
                    (requireActivity()).runOnUiThread(() -> {
                        Toast.makeText(requireContext(), requireContext().getString(R.string.create_favourite_anime_error), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void mostrarProgress(boolean mostrar) {
        loading.setVisibility(mostrar ? VISIBLE : GONE);
        imageViewAnime.setVisibility(mostrar ? GONE : VISIBLE);
        textNombre.setVisibility(mostrar ? GONE : VISIBLE);
        textDescripcion.setVisibility(mostrar ? GONE : VISIBLE);
        textAnho.setVisibility(mostrar ? GONE : VISIBLE);
        textCategorias.setVisibility(mostrar ? GONE : VISIBLE);
        textCapitulos.setVisibility(mostrar ? GONE : VISIBLE);
        toolbar.setVisibility(mostrar ? GONE : VISIBLE);
        btnFloatingFav.setVisibility(mostrar ? GONE : VISIBLE);
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

                            if (textoFinal.isEmpty())
                                textoFinal = descripcion.length() > 5 ? descripcion.substring(0, 5) : descripcion;

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