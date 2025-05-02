package com.example.nimesukiapp.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.vo.Anime;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {

    private EditText searchEditText;
    private RecyclerView animeRecyclerView;
    private AnimeAdapter animeAdapter;
    private List<Anime> allAnimes = new ArrayList<>(); // Aquí almacenas todos los animes

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_catalog, container, false);

        searchEditText = root.findViewById(R.id.search_edit_text);
        animeRecyclerView = root.findViewById(R.id.anime_recyclerview);
        animeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializamos el adaptador con los animes cargados
        animeAdapter = new AnimeAdapter(allAnimes);
        animeRecyclerView.setAdapter(animeAdapter);

        // Evento de búsqueda
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterAnimes(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return root;
    }

    private void filterAnimes(String query) {
        List<Anime> filteredList = new ArrayList<>();
        for (Anime anime : allAnimes) {
            if (anime.getNombre().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(anime);
            }
        }
        animeAdapter.updateList(filteredList);
    }

    private void loadAnimes() {
        // Aquí debes cargar los animes, por ejemplo desde una base de datos o API.
    }
}


