package com.example.tiendaropachincha;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {
    private EditText editTextUsuario, editTextContraseña;
    private Button btnLogin, btnRegistrarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializa las vistas
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextContraseña = findViewById(R.id.editTextcontraseña);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);

        // Listener para el botón de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = editTextUsuario.getText().toString();
                String contraseña = editTextContraseña.getText().toString();

                if (!usuario.isEmpty() && !contraseña.isEmpty()) {
                    new LoginTask().execute(usuario, contraseña);
                } else {
                    Toast.makeText(Login.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener para el botón de registro
        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String usuario = params[0];
            String contraseña = params[1];

            try {
                // Asegúrate de que la URL sea la correcta para el login
                URL url = new URL("http://192.168.0.107/wstienda/app/services/api_login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept", "application/json");

                // Preparar los datos a enviar
                String postData =
                        "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                                "&contraseña=" + URLEncoder.encode(contraseña, "UTF-8");

                byte[] postDataBytes = postData.getBytes("UTF-8");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(postDataBytes);
                }

                // Leer respuesta del servidor
                int responseCode = conn.getResponseCode();
                Log.d("LoginTask", "Response Code: " + responseCode);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Log.d("LoginTask", "Server Response: " + response.toString());
                return response.toString();

            } catch (Exception e) {
                Log.e("LoginTask", "Error en login", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(Login.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonResponse = new JSONObject(result);
                Log.d("LoginTask", "Full JSON Response: " + jsonResponse.toString());

                boolean success = jsonResponse.optBoolean("success", false);
                String message = jsonResponse.optString("message", "Error desconocido");

                if (success) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("LoginTask", "Error procesando respuesta", e);
                Toast.makeText(Login.this, "Error procesando respuesta del servidor", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
