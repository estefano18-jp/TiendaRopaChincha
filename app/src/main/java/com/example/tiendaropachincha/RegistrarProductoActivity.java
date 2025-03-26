package com.example.tiendaropachincha;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegistrarProductoActivity extends AppCompatActivity {

    // URL de la API para registrar el producto
    private final String URLWS = "http://192.168.0.107/wstienda/app/services/service-producto.php";

    // Queue para ejecutar las solicitudes
    private RequestQueue requestQueue;

    // Campos para capturar los datos del producto
    private EditText edtTipo, edtGenero, edtTalla, edtPrecio;
    private Button btnRegistrarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        // Inicialización de las vistas
        loadUI();

        // Configuración del clic del botón para registrar el producto
        btnRegistrarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar diálogo de confirmación antes de registrar
                confirmar();
            }
        });
    }

    // Método para inicializar los componentes de la UI
    private void loadUI() {
        edtTipo = findViewById(R.id.editTextTipo);
        edtGenero = findViewById(R.id.editTextGenero);
        edtTalla = findViewById(R.id.editTextTalla);
        edtPrecio = findViewById(R.id.editTextPrecio);
        btnRegistrarProducto = findViewById(R.id.buttonRegistrar);
    }

    // Método para mostrar diálogo de confirmación
    private void confirmar() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

        dialogo.setTitle("Tienda de Ropa");
        dialogo.setMessage("¿Registramos el producto?");
        dialogo.setCancelable(true);

        dialogo.setNegativeButton("No", null);
        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registrarProducto();
            }
        });

        dialogo.create().show();
    }

    // Método para registrar el producto
    private void registrarProducto() {
        // Inicializar la cola de solicitudes
        requestQueue = Volley.newRequestQueue(this);

        // Crear objeto JSON con los datos del producto
        JSONObject datos = new JSONObject();
        try {
            datos.put("tipo", edtTipo.getText().toString());
            datos.put("genero", edtGenero.getText().toString());
            datos.put("talla", edtTalla.getText().toString());
            datos.put("precio", edtPrecio.getText().toString());
        } catch (Exception e) {
            Log.e("ErrorJSON", e.toString());
        }

        // Crear solicitud POST utilizando JsonObjectRequest
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                URLWS,
                datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Guardado", response.toString());

                        try {
                            boolean guardado = response.getBoolean("status");
                            if (guardado) {
                                resetUI();
                                showToast("Producto registrado correctamente");
                            } else {
                                showToast("No se pudo registrar el producto");
                            }
                        } catch (Exception e) {
                            Log.e("ErrorWS", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Error en la comunicación WS");
                        Log.e("VolleyError", error.toString());
                    }
                }
        );

        // Añadir la solicitud a la cola
        requestQueue.add(jsonRequest);
    }

    // Método para mostrar mensajes Toast
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Método para limpiar los campos después de registrar
    private void resetUI() {
        edtTipo.setText("");
        edtGenero.setText("");
        edtTalla.setText("");
        edtPrecio.setText("");
    }
}