package com.example.nimesukiapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nimesukiapp.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment {

    private TextInputEditText editTextNombre, editTextActualPass, editTextNuevaPass;
    private TextInputLayout layoutActualPass, layoutNuevaPass, layoutNombre;
    private Button buttonCambiarNombre, buttonCambiarPassword;
    private SwitchMaterial switchTema;
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
        layoutActualPass = view.findViewById(R.id.layoutActualPass);
        layoutNuevaPass = view.findViewById(R.id.layoutNuevaPass);
        layoutNombre = view.findViewById(R.id.nombreLayout);

        prefs = requireContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        String nombreGuardado = prefs.getString("nombreUsuario", "");
        String tema = prefs.getString("tema", "claro");

        if (tema.equals("claro")) {
            switchTema.setChecked(false);
        } else {
            switchTema.setChecked(true);
        }

        editTextNombre.setText(nombreGuardado);

        layoutNombre.setEndIconOnClickListener(v -> {
            boolean isEnabled = editTextNombre.isEnabled();

            editTextNombre.setEnabled(!isEnabled);

            if (editTextNombre.isEnabled()) {
                layoutNombre.setEndIconDrawable(R.drawable.ic_done);
            } else {
                layoutNombre.setEndIconDrawable(R.drawable.ic_edit);
            }
        });


        buttonCambiarNombre.setOnClickListener(v -> {
            String nuevoNombre = editTextNombre.getText().toString();

            // Llama a tu servicio REST para actualizar el nombre

            prefs.edit().putString("nombreUsuario", nuevoNombre).apply();
            Toast.makeText(getContext(), getString(R.string.name_updated), Toast.LENGTH_SHORT).show();
        });

        buttonCambiarPassword.setOnClickListener(v -> {
            String actual = editTextActualPass.getText().toString();
            String nueva = editTextNuevaPass.getText().toString();

            // Aquí haz la comprobación con BCrypt y actualiza si todo va bien
        });

        switchTema.setChecked(prefs.getBoolean("oscuro", false));
        switchTema.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("oscuro", isChecked).apply();

            if (isChecked) {
                // Activar el tema oscuro
                setNightMode(true);
            } else {
                // Activar el tema claro
                setNightMode(false);
            }
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

    private void setNightMode(boolean isNightMode) {
        int nightMode = isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;

        AppCompatDelegate.setDefaultNightMode(nightMode);

        getActivity().recreate();
    }
}
