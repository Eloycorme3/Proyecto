package com.example.nimesukiapp.view.fragments;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.view.adapters.FavoritoAdapter;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.vo.Favoritos;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ListaAnimesFavoritosFragment extends Fragment {
    private ListView listView;
    private TextInputEditText searchEditText;
    private LinearLayout emptyViewFavoritos;
    private ProgressBar loading;
    private ArrayList<Favoritos> listaAnimesFavoritos = new ArrayList<>();
    private OnAnimeFavoriteSelectedListener listener;
    private FavoritoAdapter adapter;
    private String nombreUsuarioLogueado = "";
    private SharedPreferences prefs;

    public ListaAnimesFavoritosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_favorites, container, false);
        prefs = requireContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);
        listView = view.findViewById(R.id.listviewAnimesFavoritos);
        searchEditText = view.findViewById(R.id.searchTextFavoritos);
        loading = view.findViewById(R.id.progressBarLoadingFavoritos);

        if (prefs.contains("detalleFavoritos")) {
            if (prefs.getBoolean("detalleFavoritos", false)) {
                prefs.edit().putBoolean("detalleFavoritos", false).apply();
            }
        }

        mostrarProgress(true);

        cargarAnimesFavoritos();

        adapter = new FavoritoAdapter(this.getContext(), listaAnimesFavoritos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Favoritos animeFavoritoSelected = (Favoritos) parent.getItemAtPosition(position);
                if (listener != null) {
                    listener.onAnimeFavoritoSelected(animeFavoritoSelected);
                }
            }
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                mostrarProgress(true);
                String textoBusqueda = searchEditText.getText().toString();
                if (textoBusqueda.isEmpty()) {
                    cargarAnimesFavoritos();
                } else {
                    buscarAnimesFavoritos(textoBusqueda);
                }
                return true;
            }
            return false;
        });

        emptyViewFavoritos = view.findViewById(R.id.emptyViewFavorites);
        listView.setEmptyView(emptyViewFavoritos);
        emptyViewFavoritos.setVisibility(GONE);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ListaAnimesFavoritosFragment.OnAnimeFavoriteSelectedListener) {
            listener = (OnAnimeFavoriteSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " debe implementar OnAnimeFavoriteSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);
        listView.setVisibility(GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void mostrarProgress(boolean mostrar) {
        loading.setVisibility(mostrar ? VISIBLE : GONE);
        listView.setVisibility(mostrar ? GONE : VISIBLE);
    }

    private void cargarAnimesFavoritos() {
        ServicioREST servicioREST = new ServicioREST(requireContext());
        new Thread(() -> servicioREST.obtenerFavoritosPorUsuario(nombreUsuarioLogueado, new ServicioREST.OnAnimesFavoritosObtenidosListener() {
            @Override
            public void onSuccess(final ArrayList<Favoritos> animesFavoritos) {
                if (animesFavoritos != null) {
                if (isAdded()) {
                    listaAnimesFavoritos.clear();
                    listaAnimesFavoritos.addAll(animesFavoritos);

                    requireActivity().runOnUiThread(() -> {
                        if (listaAnimesFavoritos.isEmpty()) {
                            emptyViewFavoritos.setVisibility(VISIBLE);
                        } else {
                            emptyViewFavoritos.setVisibility(GONE);
                        }
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            mostrarProgress(false);
                            adapter.notifyDataSetChanged();
                        }, 1000);
                    });
                }}
            }

            @Override
            public void onError(Exception e) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), getString(R.string.load_favorite_anime_error), Toast.LENGTH_LONG).show();
                        mostrarProgress(false);
                    });
                }
            }
        })).start();
    }

    private void buscarAnimesFavoritos(String textoBusqueda) {
        ServicioREST servicioREST = new ServicioREST(requireContext());
        new Thread(() -> servicioREST.obtenerFavoritosPorNombre(nombreUsuarioLogueado, textoBusqueda, new ServicioREST.OnAnimesFavoritosObtenidosPorNombreListener() {
            @Override
            public void onSuccess(final ArrayList<Favoritos> favoritosPorNombre) {
                if (favoritosPorNombre != null) {
                    if (isAdded()) {
                        listaAnimesFavoritos.clear();
                        listaAnimesFavoritos.addAll(favoritosPorNombre);

                        requireActivity().runOnUiThread(() -> {
                            if (listaAnimesFavoritos.isEmpty()) {
                                emptyViewFavoritos.setVisibility(VISIBLE);
                            } else {
                                emptyViewFavoritos.setVisibility(GONE);
                            }
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                mostrarProgress(false);
                                adapter.notifyDataSetChanged();
                            }, 1000);
                        });
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), getString(R.string.load_favorite_anime_error), Toast.LENGTH_LONG).show()
                    );
                }
            }
        })).start();
    }

    public interface OnAnimeFavoriteSelectedListener {
        void onAnimeFavoritoSelected(Favoritos favorito);
    }
}