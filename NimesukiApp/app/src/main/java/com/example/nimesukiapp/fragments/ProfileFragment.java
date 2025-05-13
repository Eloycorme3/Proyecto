package com.example.nimesukiapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.os.LocaleList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.nimesukiapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class ProfileFragment extends Fragment {
    private TextInputEditText editTextNombre, editTextActualPass, editTextNuevaPass;
    private TextInputLayout layoutActualPass, layoutNuevaPass, layoutNombre;
    private MaterialButton buttonCambiarNombre, buttonCambiarPassword;
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
        layoutActualPass = view.findViewById(R.id.layoutActualPass);
        layoutNuevaPass = view.findViewById(R.id.layoutNuevaPass);
        layoutNombre = view.findViewById(R.id.nombreLayout);
        spinnerIdioma = view.findViewById(R.id.spinnerIdioma);

        prefs = requireContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        String nombreGuardado = prefs.getString("nombreUsuario", "");
        boolean temaOscuro = prefs.getBoolean("oscuro", false);

        switchTema.setChecked(temaOscuro);

        String idiomaGuardado = prefs.getString("idioma", "es");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.languages, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapter);

        if (idiomaGuardado.equals("es")) {
            spinnerIdioma.setSelection(0);
        } else {
            spinnerIdioma.setSelection(1);
        }

        editTextNombre.setText(nombreGuardado);

        layoutNombre.setEndIconOnClickListener(v -> {
            boolean isEnabled = editTextNombre.isEnabled();
            editTextNombre.setEnabled(!isEnabled);
            layoutNombre.setEndIconDrawable(isEnabled ? R.drawable.ic_edit : R.drawable.ic_done);
        });

        buttonCambiarNombre.setOnClickListener(v -> {
            //String nuevoNombre = editTextNombre.getText().toString();

            //cambiar nombre dao
            //prefs.edit().putString("nombreUsuario", nuevoNombre).apply();
            //Toast.makeText(getContext(), getString(R.string.name_updated), Toast.LENGTH_SHORT).show();
        });

        buttonCambiarPassword.setOnClickListener(v -> {
            //String actual = editTextActualPass.getText().toString();
            //String nueva = editTextNuevaPass.getText().toString();

            // Aquí va la lógica para cambiar la contraseña
        });

        switchTema.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("oscuro", isChecked).apply();
            setNightMode(isChecked);
        });

        spinnerIdioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String idioma = (position == 0) ? "es" : "gl";
                String idiomaActual = prefs.getString("idioma", "es");

                if (!idioma.equals(idiomaActual)) {
                    prefs.edit().putString("idioma", idioma).apply();
                    cambiarIdioma(idioma);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void cambiarIdioma(String idioma) {
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);

        Configuration configuration = getResources().getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(new LocaleList(locale));
        } else {
            configuration.setLocale(locale);
        }

        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        getActivity().recreate();
    }


    private void setNightMode(boolean isNightMode) {
        int nightMode = isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }
}
