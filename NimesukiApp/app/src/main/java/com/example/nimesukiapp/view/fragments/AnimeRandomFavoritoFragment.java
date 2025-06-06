package com.example.nimesukiapp.view.fragments;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.FavoritosManager;
import com.example.nimesukiapp.model.vo.Favoritos;
import com.example.nimesukiapp.view.adapters.PickerAdapter;
import com.example.nimesukiapp.view.adapters.PickerLayoutManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimeRandomFavoritoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimeRandomFavoritoFragment extends Fragment {
    private ImageView imageViewAnimeRandomFav;
    private MaterialTextView textNombreRandomFav, textDescripcionRandomFav,
            textAnhoRandomFav, textCategoriasRandomFav, textCapitulosRandomFav, textProgresoRandom, textValoracionRandom;
    private FloatingActionButton btnFloatingFavRandomFav;
    private ProgressBar loading;
    private RecyclerView rvHorizontalPickerRandom;
    private RatingBar ratingBarAnimeFavoritoRandom;
    private ImageButton btnGuardarCambios;
    private String imageVersionRandomFav = "?v=3";
    private boolean isExpanded = false;
    private Favoritos animeRandomFav;
    private FavoritosManager cacheManager;
    private ArrayList<String> favoritosCache;
    private PickerAdapter adapter;
    private ArrayList<String> data;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable saveRunnable;
    private static final int SAVE_DELAY = 500;
    private int lastSavedPosition = -1;
    private SharedPreferences prefs;

    public AnimeRandomFavoritoFragment(Favoritos animeFav) {
        this.animeRandomFav = animeFav;
    }

    public AnimeRandomFavoritoFragment() {}

    public static AnimeRandomFavoritoFragment newInstance(Favoritos animeFav) {
        AnimeRandomFavoritoFragment fragment = new AnimeRandomFavoritoFragment(animeFav);
        Bundle args = new Bundle();
        args.putSerializable("animeFavorito", animeFav);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_anime_random_favorito, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        prefs = requireContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        prefs.edit().putBoolean("detalleFavoritos", true).apply();

        if (getArguments() != null) {
            animeRandomFav = (Favoritos) getArguments().getSerializable("animeFavorito");
        }

        imageViewAnimeRandomFav = view.findViewById(R.id.imageViewAnimeRandomFav);
        textNombreRandomFav = view.findViewById(R.id.textNombreRandomFav);
        textDescripcionRandomFav = view.findViewById(R.id.textDescripcionRandomFav);
        textAnhoRandomFav = view.findViewById(R.id.textAnhoRandomFav);
        textCategoriasRandomFav = view.findViewById(R.id.textCategoriasRandomFav);
        textCapitulosRandomFav = view.findViewById(R.id.textCapitulosRandomFav);
        btnFloatingFavRandomFav = view.findViewById(R.id.btnFloatingFavRandomFavorito);
        loading = view.findViewById(R.id.progressBarLoadingAnimeRandomFavorito);
        rvHorizontalPickerRandom = view.findViewById(R.id.rvHorizontalPickerRandom);
        ratingBarAnimeFavoritoRandom = view.findViewById(R.id.ratingBarAnimeFavoritoRandom);
        textProgresoRandom = view.findViewById(R.id.textProgresoRandom);
        textValoracionRandom = view.findViewById(R.id.textValoracionRandom);
        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambios);

        mostrarProgress(false);

        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsingToolbarRandomFav);
        collapsingToolbar.setTitle("");

        if (animeRandomFav != null) {
            try {
                actualizarVistaAnime(animeRandomFav);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        setHorizontalPicker(0, animeRandomFav.getAnime().getCapTotales(), animeRandomFav.getCapActual());

        cacheManager = new FavoritosManager(requireContext());

        favoritosCache = cacheManager.cargarFavoritos();

        ratingBarAnimeFavoritoRandom.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    if (rating != animeRandomFav.getValoracion()) {
                        changeEnabled(true);
                    } else if (lastSavedPosition == -1) {
                        changeEnabled(false);
                    } else if (rating == animeRandomFav.getValoracion() && lastSavedPosition == animeRandomFav.getCapActual()) {
                        changeEnabled(false);
                    } else {
                        changeEnabled(true);
                    }
                }
            }
        });

        ratingBarAnimeFavoritoRandom.setRating(animeRandomFav.getValoracion());

        btnFloatingFavRandomFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFloatingFavRandomFav.setEnabled(false);
                ServicioREST servicioREST = new ServicioREST(requireContext());
                new Thread(() -> {
                    try {
                        servicioREST.eliminarFavorito(animeRandomFav.getId(), new Callback() {
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
                                        Toast.makeText(requireContext(), requireContext().getString(R.string.delete_favourite_anime_successfully), Toast.LENGTH_SHORT).show();
                                        favoritosCache.remove(animeRandomFav.getAnime().getNombre());
                                        cacheManager.guardarFavoritos(favoritosCache);
                                        AnimeRandomFragment animeRandomFragment = AnimeRandomFragment.newInstance(animeRandomFav.getAnime());
                                        mostrarProgress(true);
                                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                            requireActivity().getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .setCustomAnimations(0, 0)
                                                    .replace(R.id.fragmentContainerRandom, animeRandomFragment)
                                                    .commit();

                                            mostrarProgress(false);
                                        }, 500);
                                    });
                                } else {
                                    (requireActivity()).runOnUiThread(() ->
                                            Toast.makeText(requireContext(), requireContext().getString(R.string.delete_favourite_anime_error), Toast.LENGTH_SHORT).show()
                                    );
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    (requireActivity()).runOnUiThread(() -> btnFloatingFavRandomFav.setEnabled(true));
                }).start();
            }
        });

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSavedPosition != animeRandomFav.getCapActual() && lastSavedPosition != -1) {
                    animeRandomFav.setCapActual(lastSavedPosition);
                }
                if (ratingBarAnimeFavoritoRandom.getRating() != animeRandomFav.getValoracion()) {
                    animeRandomFav.setValoracion(ratingBarAnimeFavoritoRandom.getRating());
                }
                ServicioREST servicioREST = new ServicioREST(requireContext());
                new Thread(() -> servicioREST.actualizarFavorito(animeRandomFav, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), getString(R.string.update_favorite_error), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), getString(R.string.update_favorite_successfully), Toast.LENGTH_SHORT).show();
                            changeEnabled(false);
                        });
                    }
                })).start();
            }
        });

        changeEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setHorizontalPicker(int min, int max, int startValue) {
        data = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            data.add(String.valueOf(i));
        }

        int padding = (getResources().getDisplayMetrics().widthPixels / 2) - dpToPx(40);
        rvHorizontalPickerRandom.setPadding(padding, 0, padding, 0);
        rvHorizontalPickerRandom.setClipToPadding(false);

        PickerLayoutManager layoutManager = new PickerLayoutManager(requireContext());
        rvHorizontalPickerRandom.setLayoutManager(layoutManager);

        if (adapter == null) adapter = new PickerAdapter(requireContext());
        adapter.setData(data);
        rvHorizontalPickerRandom.setAdapter(adapter);

        layoutManager.setOnItemSelectedListener(position -> {
            adapter.setSelectedItem(position);

            if (saveRunnable != null) handler.removeCallbacks(saveRunnable);

            saveRunnable = () -> {
                lastSavedPosition = position;
                if (position != animeRandomFav.getCapActual()) {
                    changeEnabled(true);
                } else if (position == animeRandomFav.getCapActual() && ratingBarAnimeFavoritoRandom.getRating() == animeRandomFav.getValoracion()) {
                    changeEnabled(false);
                } else {
                    changeEnabled(true);
                }
            };
            handler.postDelayed(saveRunnable, SAVE_DELAY);
        });

        rvHorizontalPickerRandom.post(() -> {
            int startPosition = startValue - min;
            rvHorizontalPickerRandom.scrollToPosition(startPosition);
            adapter.setSelectedItem(startPosition);
        });
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private void changeEnabled(boolean enabled) {
        btnGuardarCambios.setEnabled(enabled);
        if (enabled) {
            btnGuardarCambios.setImageAlpha(255);
        } else {
            btnGuardarCambios.setImageAlpha(130);
        }
    }

    private void actualizarVistaAnime(Favoritos favorito) throws InterruptedException {
        if (favorito != null) {
            textNombreRandomFav.setText(favorito.getAnime().getNombre());
            textDescripcionRandomFav.setText(favorito.getAnime().getDescripcion());
            textAnhoRandomFav.setText(getString(R.string.release_year) + ": " + favorito.getAnime().getAnhoSalida());
            textCategoriasRandomFav.setText(getString(R.string.categories) + ": " + favorito.getAnime().getCategorias());
            textCapitulosRandomFav.setText(getString(R.string.episodes) + ": " + favorito.getAnime().getCapTotales());

            setupExpandableText(textDescripcionRandomFav, favorito.getAnime().getDescripcion(), 3);

            Glide.with(this)
                    .load(favorito.getAnime().getImagen() + imageVersionRandomFav)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .into(imageViewAnimeRandomFav);
        }
    }

    private void mostrarProgress(boolean mostrar) {
        loading.setVisibility(mostrar ? VISIBLE : GONE);
        imageViewAnimeRandomFav.setVisibility(mostrar ? GONE : VISIBLE);
        textNombreRandomFav.setVisibility(mostrar ? GONE : VISIBLE);
        textDescripcionRandomFav.setVisibility(mostrar ? GONE : VISIBLE);
        textAnhoRandomFav.setVisibility(mostrar ? GONE : VISIBLE);
        textCategoriasRandomFav.setVisibility(mostrar ? GONE : VISIBLE);
        textCapitulosRandomFav.setVisibility(mostrar ? GONE : VISIBLE);
        btnFloatingFavRandomFav.setVisibility(mostrar ? GONE : VISIBLE);
        textProgresoRandom.setVisibility(mostrar ? GONE : VISIBLE);
        rvHorizontalPickerRandom.setVisibility(mostrar ? GONE : VISIBLE);
        textValoracionRandom.setVisibility(mostrar ? GONE : VISIBLE);
        ratingBarAnimeFavoritoRandom.setVisibility(mostrar ? GONE : VISIBLE);
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
