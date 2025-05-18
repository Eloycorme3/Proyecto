package com.example.nimesukiapp.vista.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.LocaleList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.vista.activities.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class ProfileFragment extends Fragment {
    private TextInputEditText editTextNombre, editTextActualPass, editTextNuevaPass;
    private TextInputLayout layoutActualPass, layoutNuevaPass, layoutNombre;
    private MaterialButton btnCambiarNombre, btnCambiarPassword, btnCerrarSesion, btnEliminarCuenta;
    private SwitchMaterial switchTema;
    private Spinner spinnerIdioma;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextNombre = view.findViewById(R.id.editTextNombre);
        btnCambiarNombre = view.findViewById(R.id.buttonCambiarNombre);
        editTextActualPass = view.findViewById(R.id.editTextActualPass);
        editTextNuevaPass = view.findViewById(R.id.editTextNuevaPass);
        btnCambiarPassword = view.findViewById(R.id.buttonCambiarPassword);
        switchTema = view.findViewById(R.id.switchTema);
        layoutActualPass = view.findViewById(R.id.layoutActualPass);
        layoutNuevaPass = view.findViewById(R.id.layoutNuevaPass);
        layoutNombre = view.findViewById(R.id.nombreLayout);
        spinnerIdioma = view.findViewById(R.id.spinnerIdioma);
        btnCerrarSesion = view.findViewById(R.id.buttonCerrarSesion);
        btnEliminarCuenta = view.findViewById(R.id.buttonEliminarCuenta);

        prefs = requireContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        String nombreGuardado = prefs.getString("nombreUsuario", "");
        boolean temaOscuro = prefs.getBoolean("oscuro", false);

        switchTema.setChecked(temaOscuro);

        String idiomaGuardado = prefs.getString("idioma", "es");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.languages,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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

        btnCambiarNombre.setOnClickListener(v -> {
            //String nuevoNombre = editTextNombre.getText().toString();

            //cambiar nombre dao
            //prefs.edit().putString("nombreUsuario", nuevoNombre).apply();
            //Toast.makeText(getContext(), getString(R.string.name_updated), Toast.LENGTH_SHORT).show();
        });

        btnCambiarPassword.setOnClickListener(v -> {
            //String actual = editTextActualPass.getText().toString();
            //String nueva = editTextNuevaPass.getText().toString();

            //cambiar la contraseña
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

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.logout))
                        .setMessage(getString(R.string.logout_confirmation))
                        .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                prefs.edit().clear().apply();

                                Intent intent = new Intent(requireActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                                requireActivity().finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
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
