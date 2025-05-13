package com.example.nimesukiapp.fragments;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.adapters.FavoritoAdapter;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.models.vo.Anime;
import com.example.nimesukiapp.models.vo.Favoritos;

import java.io.IOException;
import java.util.ArrayList;

public class CatalogFavoritesFragment extends Fragment {
    private ArrayList<Favoritos> listaAnimesFavoritos = new ArrayList<>();
    private OnAnimeFavoriteSelectedListener listener;
    FavoritoAdapter adapter;
    private String nombreUSuarioLogueado = "";
    private SharedPreferences prefs;

    public CatalogFavoritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vistaFrag = inflater.inflate(R.layout.fragment_catalog_favorites, container, false);
        ListView listView = vistaFrag.findViewById(R.id.animes_favoritos_listview);

        prefs = getContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        nombreUSuarioLogueado = prefs.getString("nombreUsuario", null);

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

        return vistaFrag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CatalogFavoritesFragment.OnAnimeFavoriteSelectedListener) {
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
        ServicioREST servicioREST = new ServicioREST();
        servicioREST.obtenerFavoritosPorUsuario(nombreUSuarioLogueado, new ServicioREST.OnAnimesFavoritosObtenidosListener() {

            @Override
            public void onSuccess(ArrayList<Favoritos> animesFavoritos) {
                if (isAdded()) {
                    listaAnimesFavoritos.clear();
                    listaAnimesFavoritos.addAll(animesFavoritos);

                    requireActivity().runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), getString(R.string.load_favorite_anime_error), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void obtenerPelicula(String nombreAnime) throws IOException {

        ServicioREST servicioREST = new ServicioREST();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Anime a;
                        a = servicioREST.obtenerAnimePorNombre(nombreAnime);
                        //Looper.prepare();
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getBaseContext(), a.getNombre() + " prueba", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public interface OnAnimeFavoriteSelectedListener {
        void onAnimeFavoritoSelected(Favoritos favorito);
    }
}