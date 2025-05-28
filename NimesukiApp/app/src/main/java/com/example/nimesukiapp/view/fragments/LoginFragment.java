package com.example.nimesukiapp.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.model.vo.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.textfield.TextInputEditText;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginFragment extends Fragment {
    private TextInputEditText usernameEditText, passwordEditText;
    private MaterialButton loginButton, registerButton;
    private ImageButton toggleVisibilityButton, btnOpciones;
    private ServicioREST servicioREST;
    private SharedPreferences prefs;
    private OnLoginSuccessListener loginSuccessListener;
    private OnRegisterSuccessListener registerSuccessListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        prefs = getContext().getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        if (!prefs.contains("idioma")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nombreUsuario", null);
            editor.putString("idioma", "es");
            editor.putBoolean("oscuro", false);
            editor.apply();
        }

        ShapeableImageView imageView = view.findViewById(R.id.login_image);

        float radius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12,
                getResources().getDisplayMetrics()
        );

        ShapeAppearanceModel shapeModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(radius)
                .build();

        imageView.setShapeAppearanceModel(shapeModel);


        usernameEditText = view.findViewById(R.id.username_input);
        passwordEditText = view.findViewById(R.id.password_input);
        loginButton = view.findViewById(R.id.login_button);
        registerButton = view.findViewById(R.id.register_button);
        toggleVisibilityButton = view.findViewById(R.id.password_toggle_button);

        toggleVisibilityButton.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    toggleVisibilityButton.setImageResource(R.drawable.ic_visibility_off);
                } else {
                    passwordEditText.setTransformationMethod(null);
                    toggleVisibilityButton.setImageResource(R.drawable.ic_visibility_on);
                }

                passwordEditText.setSelection(passwordEditText.getText().length());

                isPasswordVisible = !isPasswordVisible;
            }
        });

        btnOpciones = view.findViewById(R.id.options_button);

        btnOpciones.setOnClickListener(v -> {
            mostrarDialogoConfiguracion();
        });

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), getString(R.string.enter_user_and_password), Toast.LENGTH_SHORT).show()
                );
            } else {
                loginUsuario(username, password);
            }
        });

        registerButton.setOnClickListener(v -> {
            String nombreUsuario = usernameEditText.getText().toString().trim();
            String contrasena = passwordEditText.getText().toString().trim();

            if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
                );
                return;
            }

            if (nombreUsuario.contains(" ")) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), getString(R.string.username_no_whitespace_error), Toast.LENGTH_SHORT).show()
                );
                return;
            }

            if (contrasena.contains(" ")) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), getString(R.string.password_no_whitespace_error), Toast.LENGTH_SHORT).show()
                );
                return;
            }

            servicioREST = new ServicioREST(requireContext());
            new Thread(() -> servicioREST.obtenerUsuarioPorNombre(nombreUsuario, new ServicioREST.OnUsuarioObtenidoListener() {
                @Override
                public void onSuccess(Usuario usuario) {
                    if (usuario != null) {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), getString(R.string.user_exists), Toast.LENGTH_SHORT).show()
                        );
                    }
                }

                @Override
                public void onError(Exception e) {
                    String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());
                    Usuario u = new Usuario(nombreUsuario, hashedPassword);
                    servicioREST.registrarUsuario(u, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                            );
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) {
                            if (response.isSuccessful()) {
                                new Thread(() -> servicioREST.obtenerUsuarioPorNombre(nombreUsuario, new ServicioREST.OnUsuarioObtenidoListener() {
                                    @Override
                                    public void onSuccess(Usuario usuario) {
                                        if (usuario != null) {
                                            requireActivity().runOnUiThread(() ->
                                                    Toast.makeText(requireContext(), getString(R.string.registered_successfully), Toast.LENGTH_SHORT).show()
                                            );
                                            registerSuccessListener.onRegisterSuccess(usuario);
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        requireActivity().runOnUiThread(() ->
                                                Toast.makeText(requireContext(), getString(R.string.user_not_found), Toast.LENGTH_SHORT).show()
                                        );
                                    }
                                })).start();
                            } else {
                                requireActivity().runOnUiThread(() ->
                                        Toast.makeText(requireContext(), getString(R.string.register_error), Toast.LENGTH_SHORT).show()
                                );
                            }
                        }
                    });
                }
            })).start();
        });

        return view;
    }

    private void mostrarDialogoConfiguracion() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.dialog_config, null);

        TextInputEditText editNombre = view.findViewById(R.id.edit_nombre);
        TextInputEditText editContrasena = view.findViewById(R.id.edit_contrasena);
        TextInputEditText editIp = view.findViewById(R.id.edit_ip);

        editNombre.setText(prefs.getString("nombreBD", ""));
        editContrasena.setText(prefs.getString("contrasenhaBD", ""));
        editIp.setText(prefs.getString("ip", ""));

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.configuration))
                .setView(view)
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String nombre = editNombre.getText().toString();
                    String contrasena = editContrasena.getText().toString();
                    String ip = editIp.getText().toString();

                    prefs = requireContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("nombreBD", nombre);
                    editor.putString("contrasenhaBD", contrasena);
                    editor.putString("ip", ip);
                    editor.apply();
                })
                .show();
    }

    public interface OnLoginSuccessListener {
        void onLoginSuccess(Usuario usuario);
    }

    public interface OnRegisterSuccessListener {
        void onRegisterSuccess(Usuario usuario);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginSuccessListener) {
            loginSuccessListener = (OnLoginSuccessListener) context;
        } else {
            loginSuccessListener = null;
        }

        if (context instanceof OnRegisterSuccessListener) {
            registerSuccessListener = (OnRegisterSuccessListener) context;
        } else {
            registerSuccessListener = null;
        }
    }

    private void loginUsuario(String username, String password) {
        servicioREST = new ServicioREST(requireContext());
        new Thread(() -> servicioREST.obtenerUsuarioPorNombre(username, new ServicioREST.OnUsuarioObtenidoListener() {
            @Override
            public void onSuccess(Usuario usuario) {
                if (usuario != null) {
                    if (BCrypt.checkpw(password, usuario.getContrasenha())) {
                        loginSuccessListener.onLoginSuccess(usuario);
                    } else {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), getString(R.string.user_not_found), Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), getString(R.string.get_user_error), Toast.LENGTH_SHORT).show()
                );
            }
        })).start();
    }
}