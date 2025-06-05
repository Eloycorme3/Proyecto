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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nimesukiapp.R;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.FavoritosManager;
import com.example.nimesukiapp.model.vo.Favoritos;
import com.example.nimesukiapp.view.activities.ListaAnimesFavoritosView;
import com.example.nimesukiapp.view.activities.ListaAnimesView;
import com.example.nimesukiapp.view.adapters.PickerAdapter;
import com.example.nimesukiapp.view.adapters.PickerLayoutManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimeFavoritoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimeFavoritoDetailFragment extends Fragment {
    private ImageView imageViewAnimeFav;
    private MaterialTextView textNombreFav, textDescripcionFav,
            textAnhoFav, textCategoriasFav, textCapitulosFav, textProgreso, textValoracion;
    private FloatingActionButton btnFloatingFavFavoritos;
    private ProgressBar loading;
    private MaterialToolbar toolbar;
    private RecyclerView rvHorizontalPicker;
    private RatingBar ratingBarAnimeFavorito;
    private String imageVersionFav = "?v=3";
    private boolean isExpanded = false;
    private Favoritos animeFav;
    private FavoritosManager cacheManager;
    private ArrayList<String> favoritosCache;
    private PickerAdapter adapter;
    private ArrayList<String> data;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable saveRunnable;
    private MenuItem iconGuardarCambios;
    private static final int SAVE_DELAY = 500;
    private int lastSavedPosition = -1;
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
        prefs = requireContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
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
        btnFloatingFavFavoritos = view.findViewById(R.id.btnFloatingFavFavoritos);
        loading = view.findViewById(R.id.progressBarLoadingAnimeFavoritoDetail);
        rvHorizontalPicker = view.findViewById(R.id.rvHorizontalPicker);
        ratingBarAnimeFavorito = view.findViewById(R.id.ratingBarAnimeFavorito);
        textProgreso = view.findViewById(R.id.textProgreso);
        textValoracion = view.findViewById(R.id.textValoracion);

        mostrarProgress(false);

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

            adapter = new PickerAdapter(requireContext());
            iconGuardarCambios = toolbar.getMenu().findItem(R.id.action_save);

            rvHorizontalPicker.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                        changeEnabled(false);
                    }
                }
            });

            setHorizontalPicker(0, animeFav.getAnime().getCapTotales(), animeFav.getCapActual());

            cacheManager = new FavoritosManager(requireContext());

            favoritosCache = cacheManager.cargarFavoritos();

            ratingBarAnimeFavorito.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        if (rating != animeFav.getValoracion()) {
                            changeEnabled(true);
                        } else if (lastSavedPosition == -1) {
                            changeEnabled(false);
                        } else if (rating == animeFav.getValoracion() && lastSavedPosition == animeFav.getCapActual()) {
                            changeEnabled(false);
                        } else {
                            changeEnabled(true);
                        }
                    }
                }
            });

            ratingBarAnimeFavorito.setRating(animeFav.getValoracion());

            btnFloatingFavFavoritos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnFloatingFavFavoritos.setEnabled(false);
                    ServicioREST servicioREST = new ServicioREST(requireContext());
                    new Thread(() -> {
                        try {
                            servicioREST.eliminarFavorito(animeFav.getId(), new Callback() {
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
                                            favoritosCache.remove(animeFav.getAnime().getNombre());
                                            cacheManager.guardarFavoritos(favoritosCache);
                                            AnimeDetailFragment animeDetailFragment = AnimeDetailFragment.newInstance(animeFav.getAnime());
                                            mostrarProgress(true);
                                            if (requireActivity() instanceof ListaAnimesView) {
                                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                                    requireActivity().getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .setCustomAnimations(0, 0)
                                                            .replace(R.id.fragmentContainerLista, animeDetailFragment)
                                                            .commit();

                                                    mostrarProgress(false);
                                                }, 500);
                                            } else if (requireActivity() instanceof ListaAnimesFavoritosView) {
                                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                                    requireActivity().getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .setCustomAnimations(0, 0)
                                                            .replace(R.id.fragmentContainerFavoritos, animeDetailFragment)
                                                            .commit();

                                                    mostrarProgress(false);
                                                }, 500);
                                            } else {
                                                mostrarProgress(false);
                                            }
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

                        (requireActivity()).runOnUiThread(() -> btnFloatingFavFavoritos.setEnabled(true));
                    }).start();
                }
            });
        }

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {
                if (lastSavedPosition != animeFav.getCapActual() && lastSavedPosition != -1) {
                    animeFav.setCapActual(lastSavedPosition);
                }
                if (ratingBarAnimeFavorito.getRating() != animeFav.getValoracion()) {
                    animeFav.setValoracion(ratingBarAnimeFavorito.getRating());
                }
                ServicioREST servicioREST = new ServicioREST(requireContext());
                servicioREST.actualizarFavorito(animeFav, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), getString(R.string.update_favorite_error), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), getString(R.string.update_favorite_successfully), Toast.LENGTH_SHORT).show();
                            requireActivity().getOnBackPressedDispatcher().onBackPressed();
                        });
                    }
                });

                return true;
            }
            return false;
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        changeEnabled(false);
    }

    private void setHorizontalPicker(int min, int max, int startValue) {
        data = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            data.add(String.valueOf(i));
        }

        int padding = (getResources().getDisplayMetrics().widthPixels / 2) - dpToPx(40);
        rvHorizontalPicker.setPadding(padding, 0, padding, 0);
        rvHorizontalPicker.setClipToPadding(false);

        PickerLayoutManager layoutManager = new PickerLayoutManager(requireContext());
        rvHorizontalPicker.setLayoutManager(layoutManager);

        if (adapter == null) adapter = new PickerAdapter(requireContext());
        adapter.setData(data);
        rvHorizontalPicker.setAdapter(adapter);

        layoutManager.setOnItemSelectedListener(position -> {
            adapter.setSelectedItem(position);

            if (saveRunnable != null) handler.removeCallbacks(saveRunnable);

            saveRunnable = () -> {
                lastSavedPosition = position;
                if (position != animeFav.getCapActual()) {
                    changeEnabled(true);
                } else if (position == animeFav.getCapActual() && ratingBarAnimeFavorito.getRating() == animeFav.getValoracion()) {
                    changeEnabled(false);
                } else {
                    changeEnabled(true);
                }
            };
            handler.postDelayed(saveRunnable, SAVE_DELAY);
        });

        rvHorizontalPicker.post(() -> {
            int startPosition = startValue - min;
            rvHorizontalPicker.scrollToPosition(startPosition);
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

    private void mostrarProgress(boolean mostrar) {
        loading.setVisibility(mostrar ? VISIBLE : GONE);
        imageViewAnimeFav.setVisibility(mostrar ? GONE : VISIBLE);
        textNombreFav.setVisibility(mostrar ? GONE : VISIBLE);
        textDescripcionFav.setVisibility(mostrar ? GONE : VISIBLE);
        textAnhoFav.setVisibility(mostrar ? GONE : VISIBLE);
        textCategoriasFav.setVisibility(mostrar ? GONE : VISIBLE);
        textCapitulosFav.setVisibility(mostrar ? GONE : VISIBLE);
        toolbar.setVisibility(mostrar ? GONE : VISIBLE);
        btnFloatingFavFavoritos.setVisibility(mostrar ? GONE : VISIBLE);
        textProgreso.setVisibility(mostrar ? GONE : VISIBLE);
        rvHorizontalPicker.setVisibility(mostrar ? GONE : VISIBLE);
        textValoracion.setVisibility(mostrar ? GONE : VISIBLE);
        ratingBarAnimeFavorito.setVisibility(mostrar ? GONE : VISIBLE);
    }

    private void changeEnabled(boolean enabled) {
        iconGuardarCambios.setEnabled(enabled);
        if (enabled) {
            iconGuardarCambios.getIcon().setAlpha(255);
        } else {
            iconGuardarCambios.getIcon().setAlpha(130);
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