package com.example.nimesukiapp.view.fragments;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.view.adapters.AnimeAdapter;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.vo.Anime;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ListaAnimesFragment extends Fragment {
    private ListView listView;
    private TextInputEditText searchEditText;
    private LinearLayout emptyView;
    private ProgressBar loading;
    private ArrayList<Anime> listaAnimes = new ArrayList<>();
    private OnAnimeSelectedListener listener;
    private AnimeAdapter adapter;
    private SharedPreferences prefs;

    public ListaAnimesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        prefs = requireContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        listView = view.findViewById(R.id.listviewFavoritos);
        searchEditText = view.findViewById(R.id.searchAnimes);
        loading = view.findViewById(R.id.progressBarLoading);

        if (prefs.getBoolean("detalle", false)) {
            prefs.edit().putBoolean("detalle", false).apply();
        } else {
            if (prefs.getBoolean("detalleFavoritos", false)) {
                mostrarProgress(true);

                cargarAnimes();
                prefs.edit().putBoolean("detalleFavoritos", false);
            } else {
                if (!prefs.contains("reinicio")) {
                    if (!prefs.getBoolean("login", false)) {
                        mostrarProgress(true);

                        cargarAnimes();
                    }
                } else {
                    if (!prefs.getBoolean("reinicio", false)) {
                        mostrarProgress(true);

                        cargarAnimes();
                    }
                }
            }
        }

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
                mostrarProgress(true);
                String textoBusqueda = searchEditText.getText().toString();
                if (textoBusqueda.isEmpty()) {
                    cargarAnimes();
                } else {
                    buscarAnimes(textoBusqueda);
                }
                return true;
            }
            return false;
        });

        emptyView = view.findViewById(R.id.emptyView);
        listView.setEmptyView(emptyView);
        emptyView.setVisibility(GONE);

        return view;
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

        if (prefs.getBoolean("inicio", true)) {
            prefs.edit().putBoolean("inicio", false).apply();
        } else {
            if (searchEditText.getText() != null) {
                if (!searchEditText.getText().toString().isEmpty()) {
                    if (prefs.getBoolean("cambio", false)) {
                        mostrarProgress(true);
                        prefs.edit().putBoolean("cambio", false).apply();
                    }
                    if (prefs.getBoolean("detalleFavoritos", false)) {
                        mostrarProgress(true);
                        prefs.edit().putBoolean("detalleFavoritos", false).apply();
                    }

                    buscarAnimes(searchEditText.getText().toString());
                } else {
                    if (prefs.getBoolean("cambio", false)) {
                        mostrarProgress(true);
                        prefs.edit().putBoolean("cambio", false).apply();
                    }
                    if (prefs.getBoolean("detalleFavoritos", false)) {
                        mostrarProgress(true);
                        prefs.edit().putBoolean("detalleFavoritos", false).apply();
                    }

                    cargarAnimes();
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void mostrarProgress(boolean mostrar) {
        loading.setVisibility(mostrar ? VISIBLE : GONE);
        listView.setVisibility(mostrar ? GONE : VISIBLE);
    }

    private void cargarAnimes() {
        ServicioREST servicioREST = new ServicioREST(requireContext());
        new Thread(() -> servicioREST.obtenerAnimes(new ServicioREST.OnAnimesObtenidosListener() {
            @Override
            public void onSuccess(final ArrayList<Anime> animes) {
                if (animes != null) {
                    if (isAdded()) {
                        listaAnimes.clear();
                        listaAnimes.addAll(animes);

                        requireActivity().runOnUiThread(() -> {
                            if (listaAnimes.isEmpty()) {
                                emptyView.setVisibility(VISIBLE);
                            } else {
                                emptyView.setVisibility(GONE);
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
                            Toast.makeText(getContext(), getString(R.string.load_animes_error), Toast.LENGTH_LONG).show()
                    );
                }
            }
        })).start();
    }

    private void buscarAnimes(String textoBusqueda) {
        ServicioREST servicioREST = new ServicioREST(requireContext());
        new Thread(() -> servicioREST.obtenerAnimesPorNombre(textoBusqueda, new ServicioREST.OnAnimesObtenidosPorNombreListener() {
            @Override
            public void onSuccess(final ArrayList<Anime> animesPorNombre) {
                if (animesPorNombre != null) {
                    if (isAdded()) {
                        listaAnimes.clear();
                        listaAnimes.addAll(animesPorNombre);

                        requireActivity().runOnUiThread(() -> {
                            if (listaAnimes.isEmpty()) {
                                emptyView.setVisibility(VISIBLE);
                            } else {
                                emptyView.setVisibility(GONE);
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
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), getString(R.string.load_animes_error), Toast.LENGTH_LONG).show();
                        mostrarProgress(false);
                    });
                }
            }
        })).start();
    }

    public interface OnAnimeSelectedListener {
        void onAnimeSelected(Anime anime);
    }
}