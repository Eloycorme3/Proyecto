package com.example.nimesukiapp.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.os.LocaleList;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.vo.Usuario;
import com.example.nimesukiapp.view.activities.ListaAnimesView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileFragment extends Fragment {
    private TextInputEditText editTextNombre, editTextActualPass, editTextNuevaPass;
    private MaterialButton btnCambiarNombre, btnCambiarPassword, btnCerrarSesion, btnEliminarCuenta;
    private SwitchMaterial switchTema;
    private ImageButton btnToggleVisibilityActualPass, btnToggleVisibilityNuevaPass, btnToggleEnabledText;
    private Spinner spinnerIdioma;
    private boolean isPasswordVisibleActual = false, isPasswordVisibleNueva = false, isEnabled = false;
    private String nombreUsuarioLogueado = null;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        prefs = requireContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        editTextNombre = view.findViewById(R.id.editTextNombre);
        btnCambiarNombre = view.findViewById(R.id.buttonCambiarNombre);
        editTextActualPass = view.findViewById(R.id.editTextActualPass);
        editTextNuevaPass = view.findViewById(R.id.editTextNuevaPass);
        btnCambiarPassword = view.findViewById(R.id.buttonCambiarPassword);
        switchTema = view.findViewById(R.id.switchTema);
        spinnerIdioma = view.findViewById(R.id.spinnerIdioma);
        btnCerrarSesion = view.findViewById(R.id.buttonCerrarSesion);
        btnEliminarCuenta = view.findViewById(R.id.buttonEliminarCuenta);
        btnToggleVisibilityActualPass = view.findViewById(R.id.passwordToggleButtonActualPass);
        btnToggleVisibilityNuevaPass = view.findViewById(R.id.passwordToggleButtonNuevaPass);
        btnToggleEnabledText = view.findViewById(R.id.nombreToggleEnabled);

        btnToggleVisibilityActualPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisibleActual) {
                    editTextActualPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnToggleVisibilityActualPass.setImageResource(R.drawable.ic_visibility_off);
                } else {
                    editTextActualPass.setTransformationMethod(null);
                    btnToggleVisibilityActualPass.setImageResource(R.drawable.ic_visibility_on);
                }

                editTextActualPass.setSelection(editTextActualPass.getText().length());

                isPasswordVisibleActual = !isPasswordVisibleActual;
            }
        });

        btnToggleVisibilityNuevaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisibleNueva) {
                    editTextNuevaPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnToggleVisibilityNuevaPass.setImageResource(R.drawable.ic_visibility_off);
                } else {
                    editTextNuevaPass.setTransformationMethod(null);
                    btnToggleVisibilityNuevaPass.setImageResource(R.drawable.ic_visibility_on);
                }

                editTextNuevaPass.setSelection(editTextNuevaPass.getText().length());

                isPasswordVisibleNueva = !isPasswordVisibleNueva;
            }
        });

        nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);
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

        editTextNombre.setText(nombreUsuarioLogueado);

        if (nombreUsuarioLogueado.equals(editTextNombre.getText().toString())) {
            btnCambiarNombre.setEnabled(false);
            btnCambiarNombre.setAlpha(0.5f);
        }

        btnToggleEnabledText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextNombre.setEnabled(!isEnabled);
                btnToggleEnabledText.setImageResource(isEnabled ? R.drawable.ic_edit : R.drawable.ic_done);

                isEnabled = !isEnabled;
            }
        });

        editTextNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (nombreUsuarioLogueado.equals(editTextNombre.getText().toString())) {
                    btnCambiarNombre.setEnabled(false);
                    btnCambiarNombre.setAlpha(0.5f);
                } else {
                    boolean habilitado = !s.toString().trim().isEmpty();

                    if (habilitado) {
                        btnCambiarNombre.setEnabled(true);
                        btnCambiarNombre.setAlpha(1.0f);
                    } else {
                        btnCambiarNombre.setEnabled(false);
                        btnCambiarNombre.setAlpha(0.5f);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btnCambiarNombre.setOnClickListener(v -> {
            String nuevoNombre = editTextNombre.getText().toString();
            if (!nuevoNombre.isEmpty()) {
                if (!nuevoNombre.contains(" ")) {
                    if (!nuevoNombre.equals(nombreUsuarioLogueado)) {
                        ServicioREST servicioREST = new ServicioREST(requireContext());
                        new Thread(() -> servicioREST.obtenerUsuarioPorNombre(nuevoNombre, new ServicioREST.OnUsuarioObtenidoListener() {
                            @Override
                            public void onSuccess(Usuario usuario) {
                                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), getString(R.string.user_exists), Toast.LENGTH_LONG).show());
                            }

                            @Override
                            public void onError(Exception e) {
                                String usuarioJson = prefs.getString("usuario_completo", null);
                                if (usuarioJson != null) {
                                    Gson gson = new Gson();
                                    Usuario usuarioLogueado = gson.fromJson(usuarioJson, Usuario.class);
                                    usuarioLogueado.setNombre(nuevoNombre);
                                    servicioREST.actualizarUsuario(usuarioLogueado, new Callback() {
                                        @Override
                                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                            requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), getString(R.string.name_updated_error), Toast.LENGTH_LONG).show());
                                        }

                                        @Override
                                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(requireContext(), getString(R.string.name_updated_successfully), Toast.LENGTH_LONG).show();

                                                prefs.edit().putString("nombreUsuario", nuevoNombre).apply();
                                                nombreUsuarioLogueado = prefs.getString("nombreUsuario", null);
                                                Gson gson = new Gson();
                                                String usuarioActualizadoJson = gson.toJson(usuarioLogueado);
                                                prefs.edit().putString("usuario_completo", usuarioActualizadoJson).apply();

                                                isEnabled = false;
                                                editTextNombre.setEnabled(isEnabled);
                                                btnToggleEnabledText.setImageResource(isEnabled ? R.drawable.ic_done : R.drawable.ic_edit);

                                                btnCambiarNombre.setEnabled(false);
                                                btnCambiarNombre.setAlpha(0.5f);
                                            });

                                        }
                                    });
                                } else {
                                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show());
                                }
                            }
                        })).start();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.same_username_error), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.username_no_whitespace_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.username_empty_error), Toast.LENGTH_SHORT).show();
            }
        });

        btnCambiarPassword.setOnClickListener(v -> {
            String actual = editTextActualPass.getText().toString();
            String nueva = editTextNuevaPass.getText().toString();
            if (!nueva.contains(" ")) {
                ServicioREST servicioREST = new ServicioREST(requireContext());
                new Thread(() -> servicioREST.obtenerUsuarioPorNombre(nombreUsuarioLogueado, new ServicioREST.OnUsuarioObtenidoListener() {
                    @Override
                    public void onSuccess(Usuario usuario) {
                        if (usuario != null) {
                            if (BCrypt.checkpw(actual, usuario.getContrasenha())) {
                                if (!BCrypt.checkpw(nueva, usuario.getContrasenha())) {
                                    String cotrasenhaHasheada = BCrypt.hashpw(nueva, BCrypt.gensalt());
                                    usuario.setContrasenha(cotrasenhaHasheada);
                                    servicioREST.actualizarUsuario(usuario, new Callback() {
                                        @Override
                                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                            requireActivity().runOnUiThread(() ->
                                                    Toast.makeText(requireContext(), getString(R.string.change_password_error), Toast.LENGTH_LONG).show()
                                            );
                                        }

                                        @Override
                                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(requireContext(), getString(R.string.change_password_successfully), Toast.LENGTH_LONG).show();

                                                Gson gson = new Gson();
                                                String usuarioJson = (gson.toJson(usuario));
                                                prefs.edit().putString("usuario_completo", usuarioJson);

                                                editTextActualPass.setText("");
                                                editTextNuevaPass.setText("");
                                                isPasswordVisibleNueva = false;
                                                editTextNuevaPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                                btnToggleVisibilityNuevaPass.setImageResource(R.drawable.ic_visibility_off);
                                                isPasswordVisibleActual = false;
                                                editTextActualPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                                btnToggleVisibilityActualPass.setImageResource(R.drawable.ic_visibility_off);
                                            });
                                        }
                                    });
                                } else {
                                    requireActivity().runOnUiThread(() ->
                                            Toast.makeText(requireContext(), getString(R.string.same_password_change), Toast.LENGTH_LONG).show()
                                    );
                                }
                            } else {
                                requireActivity().runOnUiThread(() ->
                                        Toast.makeText(requireContext(), getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show()
                                );
                            }
                        } else {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireContext(), getString(R.string.user_not_found), Toast.LENGTH_SHORT).show()
                            );
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), getString(R.string.get_user_error), Toast.LENGTH_SHORT).show()
                        );
                    }
                })).start();
            } else {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), getString(R.string.password_no_whitespace_error), Toast.LENGTH_LONG).show()
                );
            }
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
                                Intent intent = new Intent(requireActivity(), ListaAnimesView.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("reinicio", true);
                                startActivity(intent);

                                requireActivity().overridePendingTransition(0, 0);

                                requireActivity().finish();
                                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
            }
        });

        btnEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.delete_account))
                        .setMessage(getString(R.string.delete_account_confirmation))
                        .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requireActivity().runOnUiThread(() -> {
                                    ServicioREST servicioREST = new ServicioREST(requireContext());
                                    String usuarioJson = prefs.getString("usuario_completo", null);
                                    if (usuarioJson != null) {
                                        Gson gson = new Gson();
                                        Usuario u = gson.fromJson(usuarioJson, Usuario.class);
                                        servicioREST.eliminarUsuario(u.getIdUsuario(), new Callback() {
                                            @Override
                                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                requireActivity().runOnUiThread(() ->
                                                        Toast.makeText(requireContext(), R.string.delete_account_error, Toast.LENGTH_SHORT).show()
                                                );
                                            }

                                            @Override
                                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                requireActivity().runOnUiThread(() ->
                                                        Toast.makeText(requireContext(), R.string.delete_account_successfully, Toast.LENGTH_LONG).show()
                                                );
                                            }
                                        });
                                    } else {
                                        servicioREST.obtenerUsuarioPorNombre(nombreUsuarioLogueado, new ServicioREST.OnUsuarioObtenidoListener() {
                                            @Override
                                            public void onSuccess(Usuario usuario) {
                                                servicioREST.eliminarUsuario(usuario.getIdUsuario(), new Callback() {
                                                    @Override
                                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                        requireActivity().runOnUiThread(() ->
                                                                Toast.makeText(requireContext(), R.string.delete_account_error, Toast.LENGTH_SHORT).show()
                                                        );
                                                    }

                                                    @Override
                                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                        requireActivity().runOnUiThread(() ->
                                                                Toast.makeText(requireContext(), R.string.delete_account_successfully, Toast.LENGTH_LONG).show()
                                                        );
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                requireActivity().runOnUiThread(() ->
                                                        Toast.makeText(requireContext(), R.string.get_user_error, Toast.LENGTH_LONG).show()
                                                );
                                            }
                                        });
                                    }
                                });

                                Intent intent = new Intent(requireActivity(), ListaAnimesView.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("reinicio", true);
                                startActivity(intent);

                                requireActivity().overridePendingTransition(0, 0);

                                requireActivity().finish();
                                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
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

        requireActivity().recreate();
    }

    private void setNightMode(boolean isNightMode) {
        int nightMode = isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }
}
