package com.example.nimesukiapp.vista.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.vista.adapters.AnimeAdapter;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.models.vo.Anime;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;

public class CatalogFragment extends Fragment {
    private ListView listView;
    private TextInputEditText searchEditText;
    private ArrayList<Anime> listaAnimes = new ArrayList<>();
    private OnAnimeSelectedListener listener;
    AnimeAdapter adapter;

    public CatalogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vistaFrag = inflater.inflate(R.layout.fragment_catalog, container, false);
        listView = vistaFrag.findViewById(R.id.anime_listview);
        searchEditText = vistaFrag.findViewById(R.id.search_edit_text);

        cargarAnimes();

        adapter = new AnimeAdapter(this.getContext(), listaAnimes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Anime animeSelected = (Anime) parent.getItemAtPosition(position);
                if (listener != null) {
                    listener.onAnimeSelected(animeSelected);
                }
            }
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String textoBusqueda = searchEditText.getText().toString();
                if (!textoBusqueda.isEmpty()) {
                    buscarAnimes(textoBusqueda);
                }
                return true;
            }
            return false;
        });

        return vistaFrag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAnimeSelectedListener) {
            listener = (OnAnimeSelectedListener) context;
        } else {
            listener = null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void cargarAnimes() {
        ServicioREST servicioREST = new ServicioREST();
        new Thread(() -> servicioREST.obtenerAnimes(new ServicioREST.OnAnimesObtenidosListener() {
            @Override
            public void onSuccess(final ArrayList<Anime> animes) {
                if (isAdded()) {
                    listaAnimes.clear();
                    listaAnimes.addAll(animes);
                    requireActivity().runOnUiThread(() ->
                            adapter.notifyDataSetChanged()
                    );
                }
            }

            @Override
            public void onError(Exception e) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), getString(R.string.load_animes_error), Toast.LENGTH_LONG).show()
                    );
                }
            }
        })).start();
    }

    private void buscarAnimes(String textoBusqueda) {
        ServicioREST servicioREST = new ServicioREST();
        new Thread(() -> servicioREST.obtenerAnimesPorNombre(textoBusqueda, new ServicioREST.OnAnimesObtenidosPorNombreListener() {
            @Override
            public void onSuccess(final ArrayList<Anime> animesPorNombre) {
                if (isAdded()) {
                    listaAnimes.clear();
                    listaAnimes.addAll(animesPorNombre);
                    requireActivity().runOnUiThread(() ->
                            adapter.notifyDataSetChanged()
                    );
                }
            }

            @Override
            public void onError(Exception e) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), getString(R.string.load_animes_error), Toast.LENGTH_LONG).show()
                    );
                }
            }
        })).start();
    }

    public interface OnAnimeSelectedListener {
        void onAnimeSelected(Anime anime);
    }
}