package com.example.nimesukiapp.fragments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.nimesukiapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileFragment extends Fragment {

    private EditText editTextNombre, editTextActualPass, editTextNuevaPass;
    private Button buttonCambiarNombre, buttonCambiarPassword;
    private Switch switchTema;
    private Spinner spinnerIdioma;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextNombre = view.findViewById(R.id.editTextNombre);
        buttonCambiarNombre = view.findViewById(R.id.buttonCambiarNombre);
        editTextActualPass = view.findViewById(R.id.editTextActualPass);
        editTextNuevaPass = view.findViewById(R.id.editTextNuevaPass);
        buttonCambiarPassword = view.findViewById(R.id.buttonCambiarPassword);
        switchTema = view.findViewById(R.id.switchTema);
        spinnerIdioma = view.findViewById(R.id.spinnerIdioma);

        prefs = requireContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        String nombreGuardado = prefs.getString("nombreUsuario", "");
        String tema = prefs.getString("tema", "claro");

        if (tema.equals("claro")) {
            switchTema.setSelected(false);
        } else {
            switchTema.setSelected(true);
        }

        editTextNombre.setText(nombreGuardado);

        buttonCambiarNombre.setOnClickListener(v -> {
            String nuevoNombre = editTextNombre.getText().toString();

            // Llama a tu servicio REST para actualizar el nombre

            prefs.edit().putString("nombreUsuario", nuevoNombre).apply();
            Toast.makeText(getContext(), "Nombre actualizado", Toast.LENGTH_SHORT).show();
        });

        buttonCambiarPassword.setOnClickListener(v -> {
            String actual = editTextActualPass.getText().toString();
            String nueva = editTextNuevaPass.getText().toString();

            // Aquí haz la comprobación con BCrypt y actualiza si todo va bien
        });

        switchTema.setChecked(prefs.getBoolean("oscuro", false));
        switchTema.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("oscuro", isChecked).apply();

            // Aquí deberías reiniciar el theme, o guardar la preferencia y aplicar en el inicio
        });

        spinnerIdioma.setSelection(prefs.getString("idioma", "es").equals("es") ? 0 : 1);
        spinnerIdioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String lang = (position == 0) ? "es" : "gl";
                prefs.edit().putString("idioma", lang).apply();

                // Aquí deberías recargar los textos cambiando el Locale
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }
}
