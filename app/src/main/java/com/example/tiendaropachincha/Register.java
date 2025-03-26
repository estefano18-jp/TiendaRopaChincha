package com.example.tiendaropachincha;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Register extends AppCompatActivity {
    private EditText editNombreUsuario, editCorreoElectronico, editContrasena, editConfirmarContrasena;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        editNombreUsuario = findViewById(R.id.editUsuario);
        editCorreoElectronico = findViewById(R.id.editTextGmail);
        editContrasena = findViewById(R.id.editTextContraseña);
        editConfirmarContrasena = findViewById(R.id.editTextConfirmarContraseña);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = editNombreUsuario.getText().toString().trim();
                String correoElectronico = editCorreoElectronico.getText().toString().trim();
                String contrasena = editContrasena.getText().toString();
                String confirmarContrasena = editConfirmarContrasena.getText().toString();

                // Validation
                if (validateInputs(nombreUsuario, correoElectronico, contrasena, confirmarContrasena)) {
                    new RegisterUserTask().execute(nombreUsuario, correoElectronico, contrasena);
                }
            }
        });
    }

    private boolean validateInputs(String nombreUsuario, String correoElectronico,
                                   String contrasena, String confirmarContrasena) {
        // Validate empty fields
        if (nombreUsuario.isEmpty() || correoElectronico.isEmpty() ||
                contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(Register.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate username length
        if (nombreUsuario.length() < 4) {
            Toast.makeText(Register.this, "Nombre de usuario debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate email
        if (!Patterns.EMAIL_ADDRESS.matcher(correoElectronico).matches()) {
            Toast.makeText(Register.this, "Correo electrónico inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate password match
        if (!contrasena.equals(confirmarContrasena)) {
            Toast.makeText(Register.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate password strength
        if (contrasena.length() < 8) {
            Toast.makeText(Register.this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private class RegisterUserTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String nombreUsuario = params[0];
            String correoElectronico = params[1];
            String contrasena = params[2];

            try {
                URL url = new URL("http://192.168.0.107/wstienda/app/services/api.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String postData =
                        URLEncoder.encode("usuario", "UTF-8") + "=" + URLEncoder.encode(nombreUsuario, "UTF-8") + "&" +
                                URLEncoder.encode("correo", "UTF-8") + "=" + URLEncoder.encode(correoElectronico, "UTF-8") + "&" +
                                URLEncoder.encode("contraseña", "UTF-8") + "=" + URLEncoder.encode(contrasena, "UTF-8");

                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                // Leer la respuesta del servidor
                int responseCode = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Agregamos verificación de result
            if (result == null) {
                Toast.makeText(Register.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonResponse = new JSONObject(result);
                boolean success = jsonResponse.getBoolean("success");
                String message = jsonResponse.getString("message");

                // Mostrar mensaje dependiendo del resultado
                if (success) {
                    Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                    // Redirigir a la pantalla de Login
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish(); // Cerrar la actividad de registro
                } else {
                    Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Register.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
            }
        }
    }
}