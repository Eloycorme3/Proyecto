package com.example.nimesukiapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.activities.MainActivity;
import com.example.nimesukiapp.mock.ServicioREST;
import com.example.nimesukiapp.models.vo.Usuario;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginFragment extends Fragment {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private ServicioREST servicioREST;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = rootView.findViewById(R.id.username_input);
        passwordEditText = rootView.findViewById(R.id.password_input);
        loginButton = rootView.findViewById(R.id.login_button);
        registerButton = rootView.findViewById(R.id.register_button);

        servicioREST = new ServicioREST();

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor ingresa usuario y contraseña", Toast.LENGTH_SHORT).show();
            } else {
                loginUsuario(username, password);
            }
        });

        registerButton.setOnClickListener(v -> {
            String nombreUsuario = usernameEditText.getText().toString().trim();
            String contrasena = passwordEditText.getText().toString().trim();

            if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            servicioREST.obtenerUsuarioPorNombre(nombreUsuario, new ServicioREST.OnUsuarioObtenidoListener() {
                @Override
                public void onSuccess(Usuario usuario) {
                    if (usuario != null) {
                        Toast.makeText(requireContext(), "Ese usuario ya existe", Toast.LENGTH_SHORT).show();
                    } else {
                        String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());

                        servicioREST.registrarUsuario(requireContext(), nombreUsuario, hashedPassword, new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Registrado correctamente", Toast.LENGTH_SHORT).show();
                                    SharedPreferences prefs = requireActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                                    prefs.edit().putString("nombreUsuario", nombreUsuario).apply();

                                    requireActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, new CatalogFragment())
                                            .commit();
                                } else {
                                    Toast.makeText(requireContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
                }
            });


        });
        return rootView;

    }

    public void guardarUsuarioEnPreferencias(String nombreUsuario) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", nombreUsuario);
        editor.apply();
    }


    private void loginUsuario(String username, String password) {
        servicioREST.obtenerUsuarioPorNombre(username, new ServicioREST.OnUsuarioObtenidoListener() {
            @Override
            public void onSuccess(Usuario usuario) {
                if (usuario != null) {
                    if (BCrypt.checkpw(password, usuario.getContrasenha())) {
                        guardarUsuarioEnPreferencias(usuario.getNombre());
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
            }
        });

    }

}