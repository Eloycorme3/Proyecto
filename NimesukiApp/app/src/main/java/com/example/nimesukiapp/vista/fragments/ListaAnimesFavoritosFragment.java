package com.example.nimesukiapp.vista.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.vista.adapters.FavoritoAdapter;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.vo.Favoritos;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ListaAnimesFavoritosFragment extends Fragment {
    private ListView listView;
    private TextInputEditText searchEditText;
    private LinearLayout emptyViewFavoritos;
    private ArrayList<Favoritos> listaAnimesFavoritos = new ArrayList<>();
    private OnAnimeFavoriteSelectedListener listener;
    FavoritoAdapter adapter;
    private String nombreUsuarioLogueado = "";
    private SharedPreferences prefs;

    public ListaAnimesFavoritosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_favorites, container, false);
        listView = view.findViewById(R.id.animes_favoritos_listview);
        searchEditText = view.findViewById(R.id.search_edit_text_favorites);

        prefs = getContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);

        cargarAnimesFavoritos();

        adapter = new FavoritoAdapter(this.getContext(), listaAnimesFavoritos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Favoritos animeFavoritoSelected = (Favoritos) parent.getItemAtPosition(position);
                listener.onAnimeFavoritoSelected(animeFavoritoSelected);
            }
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String textoBusqueda = searchEditText.getText().toString();
                if (!textoBusqueda.isEmpty()) {
                    buscarAnimesFavoritos(textoBusqueda);
                }
                return true;
            }
            return false;
        });

        emptyViewFavoritos = view.findViewById(R.id.empty_view_favorites);
        listView.setEmptyView(emptyViewFavoritos);
        emptyViewFavoritos.setVisibility(View.GONE);

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
        /*
        try {
            obtenerActor(1); //funciona
            obtenerPelicula(1); //funciona
            obtenerActoresPorPelicula(1); //funciona
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void cargarAnimesFavoritos() {
        ServicioREST servicioREST = new ServicioREST(requireContext());
        new Thread(() -> servicioREST.obtenerFavoritosPorUsuario(nombreUsuarioLogueado, new ServicioREST.OnAnimesFavoritosObtenidosListener() {
            @Override
            public void onSuccess(final ArrayList<Favoritos> animesFavoritos) {
                if (isAdded()) {
                    listaAnimesFavoritos.clear();
                    listaAnimesFavoritos.addAll(animesFavoritos);

                    requireActivity().runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        if (listaAnimesFavoritos.isEmpty()) {
                            emptyViewFavoritos.setVisibility(View.VISIBLE);
                        } else {
                            emptyViewFavoritos.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), getString(R.string.load_favorite_anime_error), Toast.LENGTH_LONG).show()
                    );
                }
            }
        })).start();
    }

    private void buscarAnimesFavoritos(String textoBusqueda) {
        ServicioREST servicioREST = new ServicioREST(requireContext());
        new Thread(() -> servicioREST.obtenerFavoritosPorNombre(nombreUsuarioLogueado, textoBusqueda, new ServicioREST.OnAnimesFavoritosObtenidosPorNombreListener() {
            @Override
            public void onSuccess(final ArrayList<Favoritos> favoritosPorNombre) {
                if (isAdded()) {
                    listaAnimesFavoritos.clear();
                    listaAnimesFavoritos.addAll(favoritosPorNombre);
                    requireActivity().runOnUiThread(() ->
                            adapter.notifyDataSetChanged()
                    );
                }
            }

            @Override
            public void onError(Exception e) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), getString(R.string.load_favorite_anime_error), Toast.LENGTH_LONG).show()
                    );
                }
            }
        })).start();
    }

    public interface OnAnimeFavoriteSelectedListener {
        void onAnimeFavoritoSelected(Favoritos favorito);
    }
}