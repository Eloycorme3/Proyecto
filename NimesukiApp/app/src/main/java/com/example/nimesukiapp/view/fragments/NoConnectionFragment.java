package com.example.nimesukiapp.view.fragments;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.nimesukiapp.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NoConnectionFragment extends Fragment {
    private ProgressBar loading;
    private Button retryButton;
    private LinearLayout layout;
    private OnNetworkAvailableSuccessListener networkAvailableSuccessListener;
    private SharedPreferences prefs;
    private String nombreUsuarioLogueado = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_connection, container, false);

        prefs = requireContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);

        layout = view.findViewById(R.id.contentLayout);
        loading = view.findViewById(R.id.progressBarLoadingConnection);

        mostrarProgress(true);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            mostrarProgress(false);
        }, 1000);

        retryButton = view.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarProgress(true);
                if (isNetworkAvailable(requireContext())) {
                    if (nombreUsuarioLogueado != null && !nombreUsuarioLogueado.isEmpty()) {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            mostrarProgress(false);
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainerLista, new ListaAnimesFragment())
                                    .commit();
                            networkAvailableSuccessListener.onNetworkAvailableSuccess();
                        }, 1000);
                    } else {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainerLista, new LoginFragment())
                                    .commit();
                            prefs.edit().clear();
                            mostrarProgress(false);
                        }, 1000);
                    }
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        mostrarProgress(false);
                    }, 1000);
                }
            }
        });

        return view;
    }

    public interface OnNetworkAvailableSuccessListener {
        void onNetworkAvailableSuccess();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNetworkAvailableSuccessListener) {
            networkAvailableSuccessListener = (OnNetworkAvailableSuccessListener) context;
        } else {
            networkAvailableSuccessListener = null;
        }
    }


    private void mostrarProgress(boolean mostrar) {
        loading.setVisibility(mostrar ? VISIBLE : GONE);
        layout.setVisibility(mostrar ? GONE : VISIBLE);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false;

            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            );
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }
}