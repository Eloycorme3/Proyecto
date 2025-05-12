package com.example.nimesukiapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.adapters.AnimeAdapter;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.models.vo.Anime;

import java.io.IOException;
import java.util.ArrayList;

public class CatalogFragment extends Fragment {
    private ArrayList<Anime> listaAnimes = new ArrayList<>();

    private OnAnimeSelectedListener listener;
    AnimeAdapter adapter;

    public CatalogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vistaFrag = inflater.inflate(R.layout.fragment_catalog, container, false);
        ListView listView = vistaFrag.findViewById(R.id.anime_listview);

        cargarAnimes();

        adapter = new AnimeAdapter(this.getContext(), listaAnimes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Anime animeSelected = (Anime) parent.getItemAtPosition(position);
                listener.onAnimeSelected(animeSelected);
            }
        });

        return vistaFrag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAnimeSelectedListener) {
            listener = (OnAnimeSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " debe implementar OnAnimeSelectedListener");
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

    private void cargarAnimes() {
        ServicioREST servicioREST = new ServicioREST();
        servicioREST.obtenerAnimes(new ServicioREST.OnAnimesObtenidosListener() {
            @Override
            public void onSuccess(ArrayList<Anime> animes) {
                if (isAdded()) {
                    listaAnimes.clear();
                    listaAnimes.addAll(animes);

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
                            Toast.makeText(getContext(), getString(R.string.load_anime_error), Toast.LENGTH_LONG).show();
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

    public interface OnAnimeSelectedListener {
        void onAnimeSelected(Anime anime);
    }
}